package com.example.esbttr.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.esbttr.classes.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamsDataBaseHelper extends SQLiteOpenHelper {
    public static final String TEAMS_TABLE = "TEAMS_TABLE";
    public static final String TEAM_NAME = "TEAM_NAME";
    public static final String TEAM_DIFFERENCIAL = "TEAM_DIFFERENCIAL";

    public TeamsDataBaseHelper(@Nullable Context context) {
        super(context, "teams.db", null , 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + TEAMS_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + TEAM_NAME + " TEXT, " + TEAM_DIFFERENCIAL + " INT)";

        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean isValidEntry(Team t){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Team> allTeams = this.getAllTeams();
        for(int i=0; i< allTeams.size(); i++){
            Team currTeam = allTeams.get(i);
            if(currTeam.getName().equals(t.getName())){
                return false;
            }
        }
        return true;
    }

    public boolean addTeam(Team team){
        SQLiteDatabase db = this.getWritableDatabase();
        if(this.isValidEntry(team)){
            ContentValues cv = new ContentValues();
            cv.put(TEAM_NAME, team.getName());
            cv.put(TEAM_DIFFERENCIAL, team.getDifferencial());

            long insert = db.insert(TEAMS_TABLE, null , cv);
            if (insert == -1){
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }
    }

    //Get team from name
    public Team getTeam(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TEAMS_TABLE + " WHERE " + TEAM_NAME + " = '" + name + "'";

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            int id = cursor.getInt(0);
            String teamName = cursor.getString(1);
            int differencial = cursor.getInt(2);

            Team t = new Team(id, teamName, differencial);
            cursor.close();
            return t;
        }else{
            cursor.close();
            return null;
        }
    }

    public boolean teamExists(String name){
        return !this.isValidEntry(new Team(name));
    }
    public List<Team> getAllTeams(){
        List<Team> returnList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TEAMS_TABLE;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int differencial = cursor.getInt(2);

                Team t = new Team(id, name, differencial);
                returnList.add(t);
            }while(cursor.moveToNext());
        }else{
            // list sucks :/
        }
        cursor.close();
        return returnList;
    }

    public void deleteTeam(String teamName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TEAMS_TABLE + " WHERE " + TEAM_NAME + " = '" + teamName + "'";
        db.execSQL(query);
    }

    public void updateTeam(String initialName, String newName, String newDifferencial) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TEAMS_TABLE + " SET " + TEAM_NAME + " = '" + newName + "', " + TEAM_DIFFERENCIAL + " = '" + newDifferencial + "' WHERE " + TEAM_NAME + " = '" + initialName + "'";
        db.execSQL(query);
    }

    public void updateTeam(String initialName, String newDifferencial){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TEAMS_TABLE + " SET " + TEAM_DIFFERENCIAL + " = '" + newDifferencial + "' WHERE " + TEAM_NAME + " = '" + initialName + "'";
        db.execSQL(query);
    }

    public void deleteAllMatchesWithTeam(String teamName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + MatchesDataBaseHelper.MATCHES_TABLE + " WHERE " + MatchesDataBaseHelper.HOME_TEAM_NAME + " = '" + teamName + "' OR " + MatchesDataBaseHelper.AWAY_TEAM_NAME + " = '" + teamName + "'";
        db.execSQL(query);
    }
}
