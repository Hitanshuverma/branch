package com.example.branchint.models;

import com.example.branchint.api.Message;

import java.io.Serializable;
import java.util.List;

public class ThreadsUser implements Serializable {
    public String thread, date, user, message;
    public List<Message> list;
}
