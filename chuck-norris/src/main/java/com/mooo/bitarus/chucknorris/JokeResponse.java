package com.mooo.bitarus.chucknorris;

import com.google.gson.annotations.SerializedName;

public class JokeResponse {

    @SerializedName("response")
    private String response;

    @SerializedName("serverPort")
    private String serverPort;

    @SerializedName("language")
    private String language;

    public String getResponse() {
        return this.response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getServerPort() {
        return this.serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}