package com.example.esbttr.add_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.esbttr.ProfileActivity;
import com.example.esbttr.R;
import com.example.esbttr.classes.League;
import com.example.esbttr.databases.LeaguesDataBaseHelper;
import com.example.esbttr.view_activities.ViewTeamsActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class AddLeagueActivity extends AppCompatActivity {
    private MaterialButton addLeagueBtn;
    private EditText leagueName;
    private LeaguesDataBaseHelper leagueDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_league);
        getSupportActionBar().hide();

        addLeagueBtn = (MaterialButton) findViewById(R.id.addLeagueBtn);
        leagueDb = new LeaguesDataBaseHelper(this);
        leagueName = (EditText) findViewById(R.id.leagueName);

        //Profile View
        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent object to start the new activity
                Intent intent = new Intent(AddLeagueActivity.this, ProfileActivity.class);

                // Pass data to the new activity (optional)
                intent.putExtra("key", "value");
                String username = getIntent().getExtras().getString("username");
                intent.putExtra("username", username);
                // Start the new activity
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
            }
        });

        addLeagueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = leagueName.getText().toString();

                League l = new League(name);
                leagueDb.addLeague(l);
                setResult(2);
                finish();
            }
        });
    }
}