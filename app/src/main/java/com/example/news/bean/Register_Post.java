package com.example.news.bean;

public class Register_Post {

    private String username = null;
    private String password = null;
    private String email = null;
    private String verify = null;
    public String email() {
        return email;
    }
    public void email(String username) {
        this.email = username;
    }
    public String verify() {
        return verify;
    }
    public void verify(String username) {
        this.verify = username;
    }
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
    public Register_Post() {
        super();
        // TODO Auto-generated constructor stub
    }
}
