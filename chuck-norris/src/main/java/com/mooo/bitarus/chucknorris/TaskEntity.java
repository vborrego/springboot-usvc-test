package com.mooo.bitarus.chucknorris;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

@Entity
@NamedQueries({
        @NamedQuery(name = "TaskEntity.getAll", query = "Select e From TaskEntity e")
})
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    @Enumerated(EnumType.STRING)
    private TaskStatus state;
    private String language;
    private Date startDate;
    private Date endDate;
    private String cancellationReason;

    public TaskEntity() {
    }
    
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getId() {
        return this.id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TaskStatus getState() {
        return this.state;
    }

    public void setState(TaskStatus state) {
        this.state = state;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCancellationReason() {
        return this.cancellationReason;
    }

    public void setCancellationReason(String reason) {
        this.cancellationReason = reason;
    }
    
    @Override
    public String toString() {
        return String.format("Task %s %s", this.url, this.state.toString());
    }
}