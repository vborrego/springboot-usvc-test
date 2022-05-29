package com.mooo.bitarus.chucknorris;;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import com.google.gson.Gson;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

@Controller
public class ChuckNorrisController {
    private final Logger logger = LoggerFactory.getLogger(ChuckNorrisController.class);

    @Value("${server.port}")
    private String serverPort;

    private Environment env;

    public ChuckNorrisController(Environment env) {
        logger.info("ChuckNorrisController created");
        this.env = env;
    }

    @RequestMapping("/chucknorris")
    @ResponseBody
    // http://localhost:8080/chucknorris
    public JokeResponse chucknorris() {
        String ret = "";
        Gson gson = new Gson();
        String language = "";

        try {
            URL url = new URL("https://api.chucknorris.io/jokes/random");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            logger.info(Integer.toString(connection.getResponseCode()));

            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                Joke joke = gson.fromJson(response.toString(), Joke.class);

                List<Joke> jokesList = new ArrayList<Joke>();
                jokesList.add(joke);
                List<Joke> jokesList2 = jokesList.stream().map(item -> {
                    item.setValue(item.getValue().toUpperCase());
                    return item;
                }).collect(Collectors.toList());
                joke = jokesList2.get(0);
                ret = joke.getValue();
            }

        } catch (Exception ex) {
            logger.error("error", ex);
        }

        JokeResponse jr = new JokeResponse();
        jr.setResponse(ret);
        jr.setServerPort(serverPort);
        jr.setLanguage(language);
        return jr;
    }
}