package com.example.parkinson_disease_detection.models;

public class Result {
    private String uid;
    private String name;
    private long time;
    private String result;
    private String url;

    public Result() {}

    public Result(String uid, String name, long time, String result, String url) {
        this.uid = uid;
        this.name = name;
        this.time = time;
        this.result = result;
        this.url = url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
