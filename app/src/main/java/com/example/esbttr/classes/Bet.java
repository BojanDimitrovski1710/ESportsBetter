package com.example.esbttr.classes;

public class Bet {

    private String user;
    private String match;
    private int wager;
    private String prediction;

    public Bet(String user, String match, int wager, String prediction) {
        this.user = user;
        this.match = match;
        this.wager = wager;
        this.prediction = prediction;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public int getWager() {
        return wager;
    }

    public void setWager(int wager) {
        this.wager = wager;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }
}
