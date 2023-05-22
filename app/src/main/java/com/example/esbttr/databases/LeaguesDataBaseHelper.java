package com.example.esbttr.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import androidx.annotation.Nullable;

import com.example.esbttr.classes.League;
import com.example.esbttr.classes.Match;
import com.example.esbttr.classes.Team;

import java.util.ArrayList;
import java.util.List;

public class LeaguesDataBaseHelper extends SQLiteOpenHelper {
    public static final String LEAGUES_TABLE = "LEAGUES_TABLE";
    public static final String LEAGUE_NAME = "LEAGUE_NAME";
    public static final String MATCHES = "MATCHES";


    public LeaguesDataBaseHelper(@Nullable Context context) {
        super(context, "leagues.db", null , 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + LEAGUES_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + LEAGUE_NAME + " TEXT, " + MATCHES + " TEXT)";

        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean isValidEntry(League l){
        SQLiteDatabase db = this.getReadableDatabase();
        List<League> allLeagues = this.getAllLeauges();
        for(int i=0; i< allLeagues.size(); i++){
            League currLeague = allLeagues.get(i);
            if(currLeague.getName().equals(l.getName())){
                return false;
            }
        }
        return true;
    }

    public boolean exists(String s){
        return !this.isValidEntry(new League(s));
    }

    public League getLeague(String name){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + name + " FROM " + LEAGUES_TABLE;

        Cursor cursor = db.rawQuery(query, null);
        League l = null;
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String leagueName = cursor.getString(1);
                String matches = cursor.getString(2);

                String[] match = matches.split(",");
                List<Pair<String, String>> finalMatches = new ArrayList<>();
                for(int i=0; i< match.length; i++){
                    String[] teams = match[i].split(" vs ");
                    Pair<String, String> curr = new Pair<>(teams[0], teams[1]);
                    finalMatches.add(curr);
                }

                l = new League(id, leagueName, finalMatches);
            }while(cursor.moveToNext());
        }else{
            // league sucks :/
        }
        cursor.close();
        return l;
    }
    public boolean addLeague(League league){
        SQLiteDatabase db = this.getWritableDatabase();
        if(this.isValidEntry(league)){
            ContentValues cv = new ContentValues();
            String matches = "";
            for(int i=0; i< league.getMatches().size(); i++){
                Pair<String, String> curr = league.getMatches().get(i);
                matches += curr.first + " vs " + curr.second;
                if(i != league.getMatches().size() - 1){
                    matches += ",";
                }
            }
            cv.put(LEAGUE_NAME, league.getName());
            cv.put(MATCHES, matches);
            long insert = db.insert(LEAGUES_TABLE, null , cv);
            if (insert == -1){
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }
    }

    public List<League> getAllLeauges(){
        List<League> returnList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + LEAGUES_TABLE;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String matches = cursor.getString(2);

                List<Pair<String, String>> matchesList = new ArrayList<Pair<String, String>>();

                League m = new League(id, name, matchesList);
                returnList.add(m);
            }while(cursor.moveToNext());
        }else{
            // list sucks :/
        }
        cursor.close();
        return returnList;
    }

    public void updateLeague(String leagueNameString, String newLeagueName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + LEAGUES_TABLE + " SET " + LEAGUE_NAME + " = '" + newLeagueName + "' WHERE " + LEAGUE_NAME + " = '" + leagueNameString + "'";
        db.execSQL(query);
    }

    public void deleteLeague(String leagueNameString) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + LEAGUES_TABLE + " WHERE " + LEAGUE_NAME + " = '" + leagueNameString + "'";
        db.execSQL(query);
    }

    //Add Match to League
    public void addMatchToLeague(String leagueNameString, String matchString) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + MATCHES + " FROM " + LEAGUES_TABLE + " WHERE " + LEAGUE_NAME + " = '" + leagueNameString + "'";
        Cursor cursor = db.rawQuery(query, null);
        String matches = "";
        if(cursor.moveToFirst()){
            do{
                matches = cursor.getString(0);
            }while(cursor.moveToNext());
        }else{
            // list sucks :/
        }
        cursor.close();
        if(matches.length()!=0)
            matches += ",";
        matches += matchString;
        String query2 = "UPDATE " + LEAGUES_TABLE + " SET " + MATCHES + " = '" + matches + "' WHERE " + LEAGUE_NAME + " = '" + leagueNameString + "'";
        db.execSQL(query2);
    }
}
