package com.mooo.bitarus.chucknorris;

import java.util.Date;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import jakarta.transaction.Transactional;

@Component
public class TaskDAO {

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    private Logger logger;

    @PersistenceContext(unitName = "default")
    EntityManager em;

    public TaskDAO() {
        logger = LoggerFactory.getLogger(TaskDAO.class);
        logger.info("Created " + this.getClass().getSimpleName());
    }

    @Transactional
    public void updateLanguage(Long id, String language) {
        List<TaskEntity> tasks = this.getAllTasks();
        for (TaskEntity task : tasks) {
            if (task.getId() == id) {
                task.setState(TaskStatus.CONCLUIDA);
                task.setLanguage(language);
                task.setEndDate(new Date());
                task.setCancellationReason("");
                em.persist(task);
                em.flush();
                break;
            }
        }
    }

    @Transactional
    public void cancelTask(Long id, String reason) {
        List<TaskEntity> tasks = this.getAllTasks();
        for (TaskEntity task : tasks) {
            if (task.getId() == id) {
                task.setState(TaskStatus.CANCELADA);
                task.setCancellationReason(reason);
                task.setEndDate(new Date());
                em.persist(task);
                em.flush();
                break;
            }
        }
    }

    @Transactional
    public Long addTask(String url) {
        TaskEntity te = new TaskEntity();
        te.setUrl(url);
        te.setState(TaskStatus.EM_PROCESSAMENTO);
        te.setStartDate(new Date());
        em.persist(te);
        em.flush();
        return te.getId();
    }

    @Transactional
    public List<TaskEntity> getAllTasks() {
        return em.createNamedQuery("TaskEntity.getAll", TaskEntity.class).getResultList();
    }

}
