package com.example.myapplication;

public class User {
    public String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String email;
    public String password;


    public String date_of_birth;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public User(String name, String email,String password, String date_of_birth) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.date_of_birth = date_of_birth;
    }
    public User() {
        this.name = "";
        this.email = "";
        this.password = "";
        this.date_of_birth = "";
    }
}
