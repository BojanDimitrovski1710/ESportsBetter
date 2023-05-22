package com.example.esbttr.details_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.example.esbttr.ProfileActivity;
import com.example.esbttr.R;
import com.example.esbttr.add_activities.AddBetActivity;
import com.example.esbttr.classes.Bet;
import com.example.esbttr.classes.Match;
import com.example.esbttr.classes.Team;
import com.example.esbttr.databases.BetsDatabaseHelper;
import com.example.esbttr.databases.MatchesDataBaseHelper;
import com.example.esbttr.databases.TeamsDataBaseHelper;
import com.example.esbttr.databases.UsersDatabaseHelper;
import com.example.esbttr.view_activities.ViewTeamsActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class MatchDetailsActivity extends AppCompatActivity {

    private EditText homeTeam;
    private EditText awayTeam;
    private MaterialButton updateMatchButton;

    private RadioGroup resultRadioGroup;

    private Switch isFinishedSwitch;
    private MaterialButton placeBetButton;
    private MatchesDataBaseHelper matchesDataBaseHelper;
    private TeamsDataBaseHelper teamsDataBaseHelper;
    private BetsDatabaseHelper betsDatabaseHelper;
    private UsersDatabaseHelper usersDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_details);
        getSupportActionBar().hide();

        Intent intent = getIntent();

        homeTeam = findViewById(R.id.homeTeamName);
        awayTeam = findViewById(R.id.awayTeamName);
        resultRadioGroup = findViewById(R.id.finishedTeamGroup);
        isFinishedSwitch = findViewById(R.id.isFinished);
        placeBetButton = findViewById(R.id.placeBetButton);
        boolean finished = intent.getBooleanExtra("isFinished", false);
        if(finished == true) {
            isFinishedSwitch.setChecked(true);
            resultRadioGroup.setVisibility(View.VISIBLE);
        }else{
            isFinishedSwitch.setChecked(false);
        }
        homeTeam.setText(intent.getStringExtra("homeTeam"));
        awayTeam.setText(intent.getStringExtra("awayTeam"));

        updateMatchButton = findViewById(R.id.updateMatchBtn);
        matchesDataBaseHelper = new MatchesDataBaseHelper(this);
        teamsDataBaseHelper = new TeamsDataBaseHelper(this);
        betsDatabaseHelper = new BetsDatabaseHelper(this);
        usersDatabaseHelper = new UsersDatabaseHelper(this);

        checkAdmin();

        isFinishedSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFinishedSwitch.isChecked()){
                    resultRadioGroup.setVisibility(View.VISIBLE);
                }else{
                    resultRadioGroup.setVisibility(View.INVISIBLE);
                }
            }
        });

        //Profile View
        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent object to start the new activity
                Intent intent = new Intent(MatchDetailsActivity.this, ProfileActivity.class);

                // Pass data to the new activity (optional)
                intent.putExtra("key", "value");
                String username = getIntent().getExtras().getString("username");
                intent.putExtra("username", username);
                // Start the new activity
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
            }
        });

        placeBetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to addBetActivity
                Intent intent = new Intent(MatchDetailsActivity.this, AddBetActivity.class);
                intent.putExtra("homeTeamName", homeTeam.getText().toString());
                intent.putExtra("awayTeamName", awayTeam.getText().toString());
                intent.putExtra("username", getIntent().getExtras().getString("username"));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        updateMatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String homeName = String.valueOf(homeTeam.getText());
                String awayName = String.valueOf(awayTeam.getText());
                if(teamsDataBaseHelper.teamExists(homeName) && teamsDataBaseHelper.teamExists(awayName)){
                    // Implement Update Match Feature
                    int finished;
                    String result = "Not Finished";
                    if(isFinishedSwitch.isChecked()) {
                        finished = 1;
                        result = ((MaterialButton) findViewById(resultRadioGroup.getCheckedRadioButtonId())).getText().toString();
                        Team home = teamsDataBaseHelper.getTeam(homeName);
                        Team away = teamsDataBaseHelper.getTeam(awayName);

                        Match currMatch = matchesDataBaseHelper.findMatch(homeName, awayName);
                        List<Bet> betsForMatch = betsDatabaseHelper.getBetsForMatch(currMatch.toString());
                        for(int i=0; i<betsForMatch.size(); i++){
                            int wager = betsForMatch.get(i).getWager();
                            String prediction = betsForMatch.get(i).getPrediction();
                            String username = betsForMatch.get(i).getUser();
                            if(prediction.equals(result)){
                                usersDatabaseHelper.modifyUserFunds(username, wager);
                            }else{
                                usersDatabaseHelper.modifyUserFunds(username, -wager);
                            }
                        }
                        betsDatabaseHelper.deleteBetsForMatch(currMatch.toString());
                        if(!result.equals(currMatch.getResult())){
                            if(result.equals("Home")){
                                home.teamWin();
                                away.teamLoss();
                            }else{
                                home.teamLoss();
                                away.teamWin();
                            }
                        }

                        teamsDataBaseHelper.updateTeam(home.getName(), String.valueOf(home.getDifferencial()));
                        teamsDataBaseHelper.updateTeam(away.getName(), String.valueOf(away.getDifferencial()));
                    }else{
                        finished = 0;
                    }
                    matchesDataBaseHelper.updateMatch(homeName, awayName, intent.getStringExtra("homeTeam"), intent.getStringExtra("awayTeam"), finished, result);
                    setResult(2);
                    finish();
                }else{
                    if(!teamsDataBaseHelper.teamExists(homeName)){
                        if (!teamsDataBaseHelper.teamExists(awayName)) {
                            Toast.makeText(MatchDetailsActivity.this, "Neither Team Exists", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MatchDetailsActivity.this, "Invalid Home Team", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(MatchDetailsActivity.this, "Invalid Away Team", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void checkAdmin() {
        String username = getIntent().getExtras().getString("username");
        if(!username.equals("admin")){
            updateMatchButton.setVisibility(View.INVISIBLE);
            isFinishedSwitch.setClickable(false);
            for(int i = 0; i < resultRadioGroup.getChildCount(); i++){
                resultRadioGroup.getChildAt(i).setClickable(false);
            }
        }
    }
}
