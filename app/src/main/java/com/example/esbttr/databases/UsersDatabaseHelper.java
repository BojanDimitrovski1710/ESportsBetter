package com.example.esbttr.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.esbttr.classes.Hash;
import com.example.esbttr.classes.User;

public class UsersDatabaseHelper extends SQLiteOpenHelper {

    public static final String USERS_TABLE = "USERS_TABLE";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_FUNDS = "USER_FUNDS";
    public static final String IS_ADMIN = "IS_ADMIN";
    public static final String PASSWORD_HASH = "PASSWORD_HASH";
    public static final String IMAGE = "IMAGE";

    public UsersDatabaseHelper(@Nullable Context context) {
        super(context, "users.db", null , 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Implement create table statement
        String createTableStatement = "CREATE TABLE " + USERS_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + USER_NAME + " TEXT, " + USER_FUNDS + " INT, " + IS_ADMIN + " INT, " + PASSWORD_HASH + " TEXT, " + IMAGE + " STRING)";
        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //Add user to database
    public boolean addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_NAME, user.getUsername());
        cv.put(USER_FUNDS, user.getFunds());
        cv.put(IS_ADMIN, user.isAdmin());
        cv.put(PASSWORD_HASH, user.getPasswordHash());
        cv.put(IMAGE, user.getImageUri());

        if(!this.isValidEntry(user)){
            return false;
        }
        long insert = db.insert(USERS_TABLE, null , cv);
        return insert != -1;
    }

    //delete user from database
    public boolean deleteUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + USERS_TABLE + " WHERE " + USER_NAME + " = " + "'" + user.getUsername() + "'";
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            return true;
        }else{
            return false;
        }
    }

    //implement isValidEntry
    public boolean isValidEntry(User u){
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + USERS_TABLE;
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            do{
                String currName = cursor.getString(1);
                if(currName.equals(u.getUsername())){
                    return false;
                }
            }while(cursor.moveToNext());
        }
        return true;
    }

    //implement isValidLogin
    public boolean isValidLogin(String name, String pass){
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + USERS_TABLE;
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            do{
                String currName = cursor.getString(1);
                String passHash = cursor.getString(4);
                if(currName.equals(name) && passHash.equals(Hash.passwordHash(pass))){
                    return true;
                }
            }while(cursor.moveToNext());
        }
        return false;
    }

    //implement getUserFunds
    public int getUserFunds(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + USERS_TABLE + " WHERE " + USER_NAME + " = " + "'" + name + "'";
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            int funds = cursor.getInt(2);
            return funds;
        }else{
            return -1;
        }
    }

    public int setUserFunds(String name, int funds){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "UPDATE " + USERS_TABLE + " SET " + USER_FUNDS + " = " + funds + " WHERE " + USER_NAME + " = " + "'" + name + "'";
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            return funds;
        }else{
            return -1;
        }
    }

    public int modifyUserFunds(String name, int funds){
        SQLiteDatabase db = this.getWritableDatabase();
        int newFunds = getUserFunds(name) + funds;
        String queryString = "UPDATE " + USERS_TABLE + " SET " + USER_FUNDS + " = " + USER_FUNDS + " + " + newFunds + " WHERE " + USER_NAME + " = " + "'" + name + "'";
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            return funds;
        }else{
            return -1;
        }
    }

    //implement updateUser
    public boolean updateUser(String name, int newFunds, String newName){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "UPDATE " + USERS_TABLE + " SET " + USER_FUNDS + " = " + newFunds + ", " + USER_NAME + " = " + "'" + newName + "'" + " WHERE " + USER_NAME + " = " + "'" + name + "'";
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            return true;
        }else{
            return false;
        }
    }

    //Implement UpdateImage
    public boolean updateImage(String name, String image){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "UPDATE " + USERS_TABLE + " SET " + IMAGE + " = " + "'" + image + "'" + " WHERE " + USER_NAME + " = " + "'" + name + "'";
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            return true;
        }else{
            return false;
        }
    }

    //Implement getImage
    public String getImage(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + USERS_TABLE + " WHERE " + USER_NAME + " = " + "'" + name + "'";
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            String image = cursor.getString(5);
            return image;
        }else{
            return null;
        }
    }

}
