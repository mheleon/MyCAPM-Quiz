package com.mheleon.mycapmquiz.models;

public class UserScore {
    private String user;
    private String country;
    private String city;
    private int score;
    private int counter;
    private int shared;

    public UserScore(String user, String country, String city, int score, int counter, int shared) {
        this.user = user;
        this.country = country;
        this.city = city;
        this.score = score;
        this.counter = counter;
        this.shared = shared;
    }

    public UserScore() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getShared() {
        return shared;
    }

    public void setShared(int shared) {
        this.shared = shared;
    }
}
