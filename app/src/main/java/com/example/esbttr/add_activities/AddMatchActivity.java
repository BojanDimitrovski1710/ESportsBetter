package com.example.esbttr.add_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.esbttr.ProfileActivity;
import com.example.esbttr.R;
import com.example.esbttr.classes.Match;
import com.example.esbttr.databases.LeaguesDataBaseHelper;
import com.example.esbttr.databases.MatchesDataBaseHelper;
import com.example.esbttr.databases.TeamsDataBaseHelper;
import com.example.esbttr.view_activities.ViewTeamsActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class AddMatchActivity extends AppCompatActivity {

    private EditText homeTeamName;
    private EditText awayTeamName;
    private MaterialButton addMatchBtn;
    private TeamsDataBaseHelper teamsDataBaseHelper;
    private MatchesDataBaseHelper matchesDataBaseHelper;
    private LeaguesDataBaseHelper leaguesDataBaseHelper;
    private String leagueName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_match);
        getSupportActionBar().hide();

        teamsDataBaseHelper = new TeamsDataBaseHelper(this);
        matchesDataBaseHelper = new MatchesDataBaseHelper(this);
        leaguesDataBaseHelper = new LeaguesDataBaseHelper(this);
        homeTeamName = findViewById(R.id.homeTeamName);
        awayTeamName = findViewById(R.id.awayTeamName);
        addMatchBtn = findViewById(R.id.addMatchBtn);

        Intent intent = getIntent();
        leagueName = intent.getStringExtra("leagueName");

        //Profile View
        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent object to start the new activity
                Intent intent = new Intent(AddMatchActivity.this, ProfileActivity.class);

                // Pass data to the new activity (optional)
                intent.putExtra("key", "value");
                String username = getIntent().getExtras().getString("username");
                intent.putExtra("username", username);
                // Start the new activity
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
            }
        });

        addMatchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String homeName = String.valueOf(homeTeamName.getText());
                String awayName = String.valueOf(awayTeamName.getText());

                if(teamsDataBaseHelper.teamExists(homeName) && teamsDataBaseHelper.teamExists(awayName) && leaguesDataBaseHelper.exists(leagueName)){
                    Match m = new Match(homeName, awayName, leagueName);
                    matchesDataBaseHelper.addMatch(m);
                    leaguesDataBaseHelper.addMatchToLeague(leagueName, m.toString());

                    teamsDataBaseHelper.close();
                    matchesDataBaseHelper.close();
                    leaguesDataBaseHelper.close();
                    finish();
                }else{
                    if(!teamsDataBaseHelper.teamExists(homeName)){
                        if (!teamsDataBaseHelper.teamExists(awayName)) {
                            Toast.makeText(AddMatchActivity.this, "Neither Team Exists", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(AddMatchActivity.this, "Invalid Home Team", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(AddMatchActivity.this, "Invalid Away Team", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}