package com.example.branchint.api;

public class MessageRequestBody {
    private int thread_id;
    private String body;

    public MessageRequestBody(String thread_id, String body) {
        this.thread_id = Integer.parseInt(thread_id);
        this.body = body;
    }
}
