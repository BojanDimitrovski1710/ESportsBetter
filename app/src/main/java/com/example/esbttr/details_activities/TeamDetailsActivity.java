package com.example.esbttr.details_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.esbttr.ProfileActivity;
import com.example.esbttr.R;
import com.example.esbttr.databases.TeamsDataBaseHelper;
import com.example.esbttr.view_activities.ViewTeamsActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class TeamDetailsActivity extends AppCompatActivity {

    private EditText teamName;
    private EditText teamDiff;
    private MaterialButton updateTeamButton;
    private MaterialButton deleteTeamButton;
    private TeamsDataBaseHelper teamsDataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_details);
        getSupportActionBar().hide();

        Intent intent = getIntent();

        teamName = findViewById(R.id.teamName);
        teamDiff = findViewById(R.id.teamDifferencial);

        updateTeamButton = findViewById(R.id.updateTeamBtn);
        deleteTeamButton = findViewById(R.id.deleteTeamBtn);

        teamDiff.setText(intent.getStringExtra("teamDiff"));
        teamName.setText(intent.getStringExtra("teamName"));

        checkAdmin();

        teamsDataBaseHelper = new TeamsDataBaseHelper(this);

        //Profile View
        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent object to start the new activity
                Intent intent = new Intent(TeamDetailsActivity.this, ProfileActivity.class);

                // Pass data to the new activity (optional)
                intent.putExtra("key", "value");
                String username = getIntent().getExtras().getString("username");
                intent.putExtra("username", username);
                // Start the new activity
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
            }
        });

        updateTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement Update Team Feature
                teamsDataBaseHelper.updateTeam(intent.getStringExtra("teamName"), teamName.getText().toString(), teamDiff.getText().toString());
            }
        });

        deleteTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Implement Delete Team Feature - Delete from DB
                teamsDataBaseHelper.deleteTeam(teamName.getText().toString());
                //Delete all matches with this team
                teamsDataBaseHelper.deleteAllMatchesWithTeam(teamName.getText().toString());
            }
        });
    }

    private void checkAdmin() {
        String username = getIntent().getExtras().getString("username");
        if(!username.equals("admin")){
            updateTeamButton.setVisibility(View.GONE);
            deleteTeamButton.setVisibility(View.GONE);
            teamName.setFocusable(false);
            teamDiff.setFocusable(false);
        }
    }
}