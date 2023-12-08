package com.example.branchint.api;

import java.io.Serializable;

public class Message implements Serializable {
    private int id;
    private int thread_id;
    private int user_id;
    private Integer agent_id; // Use Integer to allow null
    private String body;
    private String timestamp;

    public Message(int id, int thread_id, int user_id, Integer agent_id, String body, String timestamp) {
        this.id = id;
        this.thread_id = thread_id;
        this.user_id = user_id;
        this.agent_id = agent_id;
        this.body = body;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getThread_id() {
        return thread_id;
    }

    public void setThread_id(int thread_id) {
        this.thread_id = thread_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Integer getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(Integer agent_id) {
        this.agent_id = agent_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    // Include getters and setters as needed
}
