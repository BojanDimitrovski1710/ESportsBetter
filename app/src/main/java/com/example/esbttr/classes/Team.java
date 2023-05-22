package com.example.esbttr.classes;

public class Team {

    private int id;
    private String name;
    private int differencial;

    public Team(){
        this.id = 0;
        this.name = "";
        this.differencial = 0;
    }

    public Team(String name){
        this.id = 0;
        this.name = name;
        this.differencial = 0;
    }

    public Team(int id, String name, int differencial){
        this.id = id;
        this.name = name;
        this.differencial = differencial;
    }

    public Team(String name, int differencial){
        this.id = -1;
        this.name = name;
        this.differencial = differencial;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", differencial=" + differencial +
                '}';
    }

    public String getName(){
        return this.name;
    }

    public int getDifferencial(){
        return this.differencial;
    }

    public void teamWin(){
        this.differencial++;
    }
    public void teamLoss(){
        this.differencial--;
    }
}
