package com.example.cardiaccorner;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Create a class to receive and store the logs
 * from the APIs response
 */
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
