package com.example.esbttr.add_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.esbttr.ProfileActivity;
import com.example.esbttr.R;
import com.example.esbttr.classes.Team;
import com.example.esbttr.databases.TeamsDataBaseHelper;
import com.example.esbttr.view_activities.ViewTeamsActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class AddTeamActivity extends AppCompatActivity {

    private MaterialButton addTeamButton;
    private TeamsDataBaseHelper teamsDb;
    private EditText teamName;
    private EditText teamDiff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);
        getSupportActionBar().hide();

        teamsDb = new TeamsDataBaseHelper(this);
        addTeamButton = (MaterialButton) findViewById(R.id.addTeamBtn);
        teamName = (EditText) findViewById(R.id.teamName);
        teamDiff = (EditText) findViewById(R.id.teamDifferencial);

        //Profile View
        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent object to start the new activity
                Intent intent = new Intent(AddTeamActivity.this, ProfileActivity.class);

                // Pass data to the new activity (optional)
                intent.putExtra("key", "value");
                String username = getIntent().getExtras().getString("username");
                intent.putExtra("username", username);
                // Start the new activity
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
            }
        });

        addTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(teamName.getText());
                int differencial = Integer.parseInt(teamDiff.getText().toString());
                Team t = new Team(name, differencial);

                teamsDb.addTeam(t);
                teamsDb.close();
                Intent intent=new Intent();
                intent.putExtra("MESSAGE", "msg");
                setResult(2);
                finish();
            }
        });
    }
}