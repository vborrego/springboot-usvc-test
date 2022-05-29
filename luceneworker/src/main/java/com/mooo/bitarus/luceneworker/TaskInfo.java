package com.mooo.bitarus.luceneworker;

import com.google.gson.annotations.SerializedName;

public class TaskInfo {
    @SerializedName("id")
    private Integer id;

    @SerializedName("url")
    private String url;

    @SerializedName("state")
    private String state;

    public Integer getId() {
        return this.id;
    }

    public void setResponse(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
