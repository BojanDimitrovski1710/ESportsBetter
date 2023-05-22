package com.example.esbttr.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.esbttr.classes.Match;
import com.example.esbttr.classes.Team;

import java.util.ArrayList;
import java.util.List;

public class MatchesDataBaseHelper extends SQLiteOpenHelper {
    public static final String MATCHES_TABLE = "MATCHES_TABLE";
    public static final String HOME_TEAM_NAME = "HOME_TEAM_NAME";
    public static final String AWAY_TEAM_NAME = "AWAY_TEAM_NAME";

    public static final String LEAGUE_NAME = "LEAGUE_NAME";
    public static final String IS_FINISHED = "IS_FINISHED";

    public static final String RESULT = "RESULT";


    public MatchesDataBaseHelper(@Nullable Context context) {
        super(context, "matches.db", null , 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + MATCHES_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + HOME_TEAM_NAME + " TEXT, " + AWAY_TEAM_NAME + " TEXT, " + LEAGUE_NAME + " TEXT, " + IS_FINISHED + " INT, " + RESULT + " TEXT)";

        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean isValidEntry(Match m){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Match> allMatches = this.getAllMatches();
        for(int i=0; i< allMatches.size(); i++){
            Match currMatch = allMatches.get(i);
            if(currMatch.getHome().equals(m.getHome()) && currMatch.getAway().equals(m.getAway()) && currMatch.isFinished() == m.isFinished()){
                return false;
            }
        }
        return true;
    }

    //Implement findMatch by home and away team names
    public Match findMatch(String home, String away){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + MATCHES_TABLE + " WHERE " + HOME_TEAM_NAME + " = " + home + " AND " + AWAY_TEAM_NAME + " = " + away;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            int id = cursor.getInt(0);
            String homeTeam = cursor.getString(1);
            String awayTeam = cursor.getString(2);
            String league = cursor.getString(3);
            int isFinished = cursor.getInt(4);
            String result = cursor.getString(5);
            Match m = new Match(id, homeTeam, awayTeam, league, isFinished, result);
            return m;
        }else{
            return null;
        }
    }

    public boolean addMatch(Match match){
        SQLiteDatabase db = this.getWritableDatabase();
        if(this.isValidEntry(match)){
            ContentValues cv = new ContentValues();

            cv.put(HOME_TEAM_NAME, match.getHome());
            cv.put(AWAY_TEAM_NAME, match.getAway());
            cv.put(LEAGUE_NAME, match.getLeague());
            cv.put(IS_FINISHED, match.isFinished());
            cv.put(RESULT, match.getResult());

            long insert = db.insert(MATCHES_TABLE, null , cv);
            if (insert == -1){
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }
    }

    public List<Match> getAllMatches(){
        List<Match> returnList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + MATCHES_TABLE;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String home = cursor.getString(1);
                String away = cursor.getString(2);
                String league = cursor.getString(3);
                int isFinished = cursor.getInt(4);
                String result = cursor.getString(5);

                Match m = new Match(id, home, away, league, isFinished, result);
                returnList.add(m);
            }while(cursor.moveToNext());
        }else{
            // list sucks :/
        }
        cursor.close();
        return returnList;
    }

    public void updateMatch(String homeTeam, String awayTeam, String initialHomeName, String initialAwayName, int isFinished, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Update match with all of the given maramaters
        String query = "UPDATE " + MATCHES_TABLE + " SET " + HOME_TEAM_NAME + " = '" + homeTeam + "', " + AWAY_TEAM_NAME + " = '" + awayTeam + "', " + IS_FINISHED + " = " + isFinished + ", " + RESULT + " = '" + result + "' WHERE " + HOME_TEAM_NAME + " = '" + initialHomeName + "' AND " + AWAY_TEAM_NAME + " = '" + initialAwayName + "'";
        db.execSQL(query);
    }

    public List<Match> getAllMatchesFromLeague(String leagueName) {
        List<Match> returnList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + MATCHES_TABLE + " WHERE " + LEAGUE_NAME + " = '" + leagueName + "'";

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String home = cursor.getString(1);
                String away = cursor.getString(2);
                String league = cursor.getString(3);
                int isFinished = cursor.getInt(4);

                Match m = new Match(id, home, away, league, isFinished);
                returnList.add(m);
            }while(cursor.moveToNext());
        }else{
            // list sucks :/
        }
        cursor.close();
        return returnList;
    }

    public void deleteAllMatchesFromLeague(String leagueNameString) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + MATCHES_TABLE + " WHERE " + LEAGUE_NAME + " = '" + leagueNameString + "'";
        db.execSQL(query);
    }
}
