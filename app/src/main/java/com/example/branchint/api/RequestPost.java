package com.example.branchint.api;

public class RequestPost {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RequestPost(String email, String key) {
        this.username = email;
        this.password = key;
    }

    String username;
    String password;
}
