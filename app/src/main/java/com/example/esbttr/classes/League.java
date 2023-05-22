package com.example.esbttr.classes;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class League {
    private int id;
    private String name;
    private List<Pair<String, String>> matches;

    public League(int id, String name, List<Pair<String, String>> matchesList){
        this.id = id;
        this.name = name;
        this.matches = matchesList;
    }

    public League(String name){
        this.name = name;
        this.matches = new ArrayList<>();
    }


    public League(String name, List<Pair<String, String>> matches){
        this.name = name;
        this.matches = matches;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Pair<String, String>> getMatches() {
        return matches;
    }

    public void setMatches(List<Pair<String, String>> matches) {
        this.matches = matches;
    }
}
