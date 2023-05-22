package com.example.esbttr.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.esbttr.classes.Bet;
import com.example.esbttr.classes.User;
import com.example.esbttr.details_activities.MatchDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class BetsDatabaseHelper extends SQLiteOpenHelper {

    private static final String BETS_TABLE = "BETS_TABLE";
    private static final String MATCH = "MATCH_NAME";
    private static final String USER = "USER";
    private static final String WAGER = "WAGER";
    private static final String PREDICTION = "PREDICTION";


    public BetsDatabaseHelper(@Nullable Context context) {
        super(context, "bets.db", null , 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Implement onCreate for the bets table
        String createTableStatement = "CREATE TABLE " + BETS_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + MATCH + " TEXT, " + USER + " TEXT, " + WAGER + " INT, " + PREDICTION + " TEXT)";
        db.execSQL(createTableStatement);

    }

    //Implement getBetsForUser
    public List<Bet> getBetsForUser(String user){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + MATCH + ", " + WAGER + ", " + PREDICTION + " FROM " + BETS_TABLE + " WHERE " + USER + " = " + user;
        Cursor cursor = db.rawQuery(query, null);
        List<Bet> bets = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                String match = cursor.getString(0);
                int wager = cursor.getInt(1);
                String prediction = cursor.getString(2);

                bets.add(new Bet(user, match, wager, prediction));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bets;
    }

    //Implement getBetsForMatch
    public List<Bet> getBetsForMatch(String match){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + USER + ", " + WAGER + ", " + PREDICTION + " FROM " + BETS_TABLE + " WHERE " + MATCH + " = " + match;
        Cursor cursor = db.rawQuery(query, null);
        List<Bet> bets = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                String user = cursor.getString(0);
                int wager = cursor.getInt(1);
                String prediction = cursor.getString(2);

                bets.add(new Bet(user, match, wager, prediction));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bets;
    }

    //Implement deleteBetsForMatch
    public void deleteBetsForMatch(String match){
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + BETS_TABLE + " WHERE " + MATCH + " = " + match;
        db.execSQL(query);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean isValidEntry(Bet bet){
        String query = "SELECT * FROM " + BETS_TABLE + " WHERE " + MATCH + " = " + bet.getMatch() + " AND " + USER + " = " + bet.getUser();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public boolean addBet(Bet bet) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(this.isValidEntry(bet)){
            ContentValues cv = new ContentValues();
            cv.put(MATCH, bet.getMatch());
            cv.put(USER, bet.getUser());
            cv.put(WAGER, bet.getWager());
            cv.put(PREDICTION, bet.getPrediction());

            long insert = db.insert(BETS_TABLE, null , cv);
            if (insert == -1){
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }
    }
}
