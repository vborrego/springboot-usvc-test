package com.mooo.bitarus.luceneworker;

import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class Lucene {
    private static final String LANGUAGE2 = "language";
    private static final String BODY = "body";
    private final Logger logger = LoggerFactory.getLogger(Lucene.class);
    private ExecutorService executorsService = Executors.newFixedThreadPool(1);

    public class ShutdownRunnable implements Runnable {
        private String shutdownUrl;
        private Logger logger;

        public ShutdownRunnable(String shutdownUrl, Logger logger) {
            this.shutdownUrl = shutdownUrl;
            this.logger = logger;
        }

        @Override
        public void run() {
            try {
                logger.info("### Called run in ShutdownRunnable");
                Thread.sleep(30000);
                logger.info("### After sleep in ShutdownRunnable");
                URL url = new URL(this.shutdownUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.connect();
                Thread.sleep(5000);
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Lucene(Environment env, ServletWebServerApplicationContext webServerAppCtxt) {
        logger.info("Lucene created");
        String taskId = env.getProperty("taskid");
        logger.info(String.format("H2 taskid %s", taskId));
        TaskInfo taskInfo = null;
        Gson gson = new Gson();
        String shutdownUrl = "";

        try {
            shutdownUrl = String.format("http://%s:%s/actuator/shutdown",
                    InetAddress.getLocalHost().getHostName(), webServerAppCtxt.getWebServer().getPort());
            logger.info(String.format("Shutdown URL %s", shutdownUrl));

            String taskInfoResponse = this.getContent("http://localhost:8111/gettask/" + taskId);
            taskInfo = gson.fromJson(taskInfoResponse.toString(), TaskInfo.class);

            logger.info("Getting " + taskInfo.getUrl());
            String documentAnalyze = this.getContent(taskInfo.getUrl());

            FSDirectory dir = FSDirectory.open(Paths.get("/tmp"));
            StandardAnalyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);

            IndexWriter indexWriter = new IndexWriter(dir, indexWriterConfig);
            indexWriter.deleteAll();
            String dictBase = env.getProperty("dict.base"); //"/home/vitor/Documents/microservices/";
            indexWriter.addDocument(this.createDictionaryDocument("pt", dictBase + "/pt_clean.txt"));
            indexWriter.addDocument(this.createDictionaryDocument("en", dictBase + "en_clean.txt"));
            indexWriter.addDocument(this.createDictionaryDocument("fr", dictBase + "fr_clean.txt"));
            indexWriter.addDocument(this.createDictionaryDocument("de", dictBase + "de_clean.txt"));
            indexWriter.addDocument(this.createDictionaryDocument("it", dictBase + "it_clean.txt"));
            indexWriter.commit();
            indexWriter.close();
            IndexReader reader = DirectoryReader.open(dir);
            logger.info(String.format("Num docs %d ", reader.numDocs()));
            IndexSearcher searcher = new IndexSearcher(reader);

            String language = this.identifyLanguage(documentAnalyze, searcher, reader);
            if (language != "") {
                logger.info("Got language " + language + " for " + taskInfo.getUrl());
                String updateResp = this.getContent("http://localhost:8111/setlanguage/" + taskId + "/" + language);
            } else {
                this.getContent("http://localhost:8111/canceltask/" + taskId + "/LanguageNotIdentified");
            }

        } catch (Exception ex) {
            logger.error("error in lucene constructor", ex);
            try {
                this.getContent("http://localhost:8111/canceltask/" + taskId + "/UnexpectedError" );
            } catch (Exception e) {
                logger.error("error in lucene constructor #2 ", e.toString());
            }
        }
        logger.info("Constructor end!");
        executorsService.execute(new ShutdownRunnable(shutdownUrl, logger));
    }

    private String getContent(String urlpath) throws IOException {
        URL url = new URL(urlpath);
        StringBuilder response = new StringBuilder();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {

            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

        } catch (Exception ex) {
        }

        return response.toString();
    }

    private String identifyLanguage(String terms, IndexSearcher searcher, IndexReader reader) throws Exception {
        String language = "";
        float score = 0;
        Builder builder = new BooleanQuery.Builder();
        String cleaned = this.cleanup(terms);
        String[] splitted = cleaned.split(" ");
        int countTerms = 0;

        for (String term : splitted) {
            builder.add(new TermQuery(new Term(BODY, term)), Occur.SHOULD);
            countTerms++;
            if (countTerms > 1000) {
                break;
            }
        }
        BooleanQuery query = builder.build();

        TopDocs topDocs = searcher.search(query, 100);
        for (ScoreDoc sd : topDocs.scoreDocs) {
            Document doc = reader.document(sd.doc);
            if (sd.score > score) {
                score = sd.score;
                language = doc.getField(LANGUAGE2).stringValue();
            }
        }

        return language;
    }

    private String cleanup(String terms) {
        String ret = terms;
        ret = ret.replaceAll("\\.", " ");
        ret = ret.replaceAll("\\'", " ");
        ret = ret.replaceAll(",", " ");
        ret = ret.replaceAll(";", " ");
        ret = ret.replaceAll(":", " ");
        ret = ret.replaceAll("!", " ");
        ret = ret.replaceAll("\\?", " ");
        ret = ret.toLowerCase();
        return ret;
    }

    private Document createDictionaryDocument(String language, String filename)
            throws FileNotFoundException, IOException {
        Document document = new Document();
        document.add(new TextField(LANGUAGE2, language, Store.YES));
        document.add(new TextField(BODY, new FileReader(filename)));
        return document;
    }
}
