package com.example.esbttr.view_activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.esbttr.HomeActivity;
import com.example.esbttr.ProfileActivity;
import com.example.esbttr.R;
import com.example.esbttr.add_activities.AddTeamActivity;
import com.example.esbttr.classes.Team;
import com.example.esbttr.databases.TeamsDataBaseHelper;
import com.example.esbttr.details_activities.TeamDetailsActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ViewTeamsActivity extends AppCompatActivity {

    private TeamsDataBaseHelper teamsDb;
    private TableLayout teamsTable;
    private MaterialButton addTeamButton;
    private List<Team> teamList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_teams);
        getSupportActionBar().hide();

        teamsDb = new TeamsDataBaseHelper(this);
        teamsTable = (TableLayout) findViewById(R.id.teamsTable);
        addTeamButton = (MaterialButton) findViewById(R.id.addTeamButton);
        teamList = teamsDb.getAllTeams();

        checkAdmin();

        for(int i=0; i<teamList.size(); i++) {
            Team currTeam = teamList.get(i);
            addTeamToTable(currTeam);
        }

        //Profile View
        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent object to start the new activity
                Intent intent = new Intent(ViewTeamsActivity.this, ProfileActivity.class);

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
            public void onClick(View view) {
                // Create an Intent object to start the new activity
                Intent intent = new Intent(ViewTeamsActivity.this, AddTeamActivity.class);

                // Start the new activity
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
            }
        });
    }

    private void checkAdmin() {
        String username = getIntent().getExtras().getString("username");
        if(!username.equals("admin")){
            addTeamButton.setVisibility(View.INVISIBLE);

        }
    }

    private void addTeamToTable(Team currTeam){
        TextView textView = new TextView(this);
        textView.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));
        textView.setText(currTeam.getName());
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Create an Intent object to start the new activity
                Intent intent = new Intent(ViewTeamsActivity.this, TeamDetailsActivity.class);

                // Pass data to the new activity (optional)
                intent.putExtra("teamName", currTeam.getName());
                intent.putExtra("teamDiff", String.valueOf(currTeam.getDifferencial()));
                intent.putExtra("username", getIntent().getExtras().getString("username"));

                // Start the new activity
                startActivity(intent);
            }
        });

        // Get the current layout parameters of the text view
        TableLayout.LayoutParams layoutParams = (TableLayout.LayoutParams) textView.getLayoutParams();

        // Set the margins
        float marginInDp = 2.5f; // Set the margin in dp (or pixels if you prefer)
        float scale = getResources().getDisplayMetrics().density;
        int marginInPixels = (int) (marginInDp * scale + 0.5f); // Convert dp to pixels
        layoutParams.setMargins(0, marginInPixels, 0, marginInPixels);
        layoutParams.gravity = Gravity.CENTER;
        // Apply the modified layout parameters to the text view
        textView.setLayoutParams(layoutParams);

        teamsTable.addView(textView);
    }

    //On Resume, refresh the table
    @Override
    protected void onResume() {
        super.onResume();
        teamsTable.removeAllViews();
        teamList = teamsDb.getAllTeams();

        for(int i=0; i<teamList.size(); i++) {
            Team currTeam = teamList.get(i);
            addTeamToTable(currTeam);
        }
    }
}