package com.example.esbttr.classes;

import com.google.firebase.auth.FirebaseUser;

public class User {

    private String username;
    private String email;
    private String password;
    private boolean admin;
    private int funds;

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    private String imageUri;

    private FirebaseUser user;

    public User(String username, String email, String password, boolean admin, int funds) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.admin = admin;
        this.funds = funds;
    }

    //Constructor from the string format that we get from our toString() method
    public User(String userString){
        String[] userArray = userString.split(",");
        this.username = userArray[0].substring(10);
        this.email = userArray[1].substring(7);
        this.admin = Boolean.parseBoolean(userArray[2].substring(7));
        this.funds = Integer.parseInt(userArray[3].substring(7));
    }

    public User(FirebaseUser fb){
        this.username = fb.getDisplayName();
        this.email = fb.getEmail();
        this.password = "";
        this.admin = false;
        this.funds = 1000;
        this.user = fb;
        this.imageUri = fb.getPhotoUrl().toString();
    }

    //Override toString
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", admin=" + admin +
                ", funds=" + funds +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int isAdmin() {
        if(this.admin){
            return 1;
        }else{
            return 0;
        }
    }

    public void setAdmin() {
        this.admin = true;
    }

    public void removeAdmin() {
        this.admin = false;
    }

    public int getFunds() {
        return funds;
    }

    public void setFunds(int funds) {
        this.funds = funds;
    }

    public String getPasswordHash() {
        return Hash.passwordHash(this.password);
    }
}
