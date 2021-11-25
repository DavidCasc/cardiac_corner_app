package com.example.cardiaccorner;

import java.util.ArrayList;
import java.util.Arrays;

public class LogsResponse {
    ArrayList<Entry> logs;

    public ArrayList<Entry> getLogs() {
        return logs;
    }

    public void setLogs(ArrayList<Entry> logs) {
        this.logs = logs;
    }

    @Override
    public String toString() {
        return "LogsResponse{" +
                "logs=" + logs +
                '}';
    }
}
