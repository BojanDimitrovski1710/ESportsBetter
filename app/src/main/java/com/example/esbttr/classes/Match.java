package com.example.esbttr.classes;

public class Match {

    private int id;
    private String home;
    private String away;

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    private String league;

    private boolean isFinished;

    private String result;

    public Match(String h, String a, String l){
        this.id = -1;
        this.away = a;
        this.home = h;
        this.league = l;
        this.isFinished = false;
        this.result = "";
    }

    public Match(int id, String home, String away, String league, int isFinished) {
        this.id = id;
        this.home = home;
        this.away = away;
        this.league = league;
        if(isFinished > 0){
            this.isFinished = true;
        }else{
            this.isFinished = false;
        }
        this.result = "Not Finished";
    }

    public Match(int id, String home, String away, String league, int isFinished, String result) {
        this.id = id;
        this.home = home;
        this.away = away;
        this.league = league;
        if(isFinished > 0){
            this.isFinished = true;
        }else{
            this.isFinished = false;
        }
        this.result = result;
    }

    public void finish(){
        this.isFinished = true;

    }

    //Override toString() method to display match in a list
    @Override
    public String toString() {
        return home + " vs " + away;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getAway() {
        return away;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished, String result) {
        isFinished = finished;
        this.result = result;
    }

    public String getResult(){
        return this.result;
    }
}
