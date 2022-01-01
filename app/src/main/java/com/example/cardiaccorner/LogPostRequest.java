package com.example.cardiaccorner;

public class LogPostRequest {
    private Entry log;
    private String username;

    public LogPostRequest(Entry log, String username) {
        this.log = log;
        this.username = username;
    }

    public Entry getLog() {
        return log;
    }

    public void setLog(Entry log) {
        this.log = log;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
