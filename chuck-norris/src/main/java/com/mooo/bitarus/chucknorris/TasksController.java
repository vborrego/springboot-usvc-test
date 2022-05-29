package com.mooo.bitarus.chucknorris;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

@Controller
public class TasksController {
    private final Logger logger = LoggerFactory.getLogger(TasksController.class);
    private TaskDAO taskDAO;
    private Environment env;

    public TasksController(Environment env, TaskDAO taskDAO) {
        logger.debug("Tasks controller created.");
        this.taskDAO = taskDAO;
        this.env = env;
    }

    @RequestMapping("/alltasks")
    public String allTasks(Model model) {
        List<TaskEntity> tasks = this.taskDAO.getAllTasks();
        model.addAttribute("tasks", tasks);
        return "alltasks"; // template alltasks.html
    }

    @GetMapping("/gettask/{id}")
    @ResponseBody
    public TaskEntity getTask(@PathVariable long id) {
        List<TaskEntity> tasks = this.taskDAO.getAllTasks();
        TaskEntity ret = null;
        for (TaskEntity t : tasks) {
            if (t.getId() == id) {
                ret = t;
                break;
            }
        }
        return ret;
    }

    @GetMapping("/setlanguage/{id}/{language}")
    @ResponseBody
    public void getTask(@PathVariable long id, @PathVariable String language) {
        this.taskDAO.updateLanguage(id, language);
    }

    @GetMapping("/canceltask/{id}/{reason}")
    @ResponseBody
    public void cancelTask(@PathVariable long id, @PathVariable String reason) {
        this.taskDAO.cancelTask(id, reason);
    }

    @RequestMapping("/createtask")
    public String createTask(@RequestParam(value = "url", defaultValue = "") String url,
            Model model) throws IOException {
        logger.info("Tasks endpoint called.");
        Long id = -1L;
        if (url.length() > 0) {
            try {
                id = this.taskDAO.addTask(url);
                logger.info(String.format("Created task with id %d", id));
            } catch (Exception ex) {
                logger.error("Tasks controller error", ex);
            }
        }

        model.addAttribute("url", url);
        model.addAttribute("id", id);
        String baseLucene = this.env.getProperty("base.lucene");
        if (id > 0) {
            Runtime.getRuntime()
                    .exec("java -Dtaskid=" + id + " -jar "+ baseLucene +"/target/luceneworker-0.1.0.jar");
        }
        return "createtask"; // template createtask.html 
    }

}
