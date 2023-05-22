package com.example.esbttr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.esbttr.classes.Match;
import com.example.esbttr.databases.MatchesDataBaseHelper;
import com.example.esbttr.databases.TeamsDataBaseHelper;
import com.example.esbttr.classes.Team;
import com.example.esbttr.details_activities.LeagueDetailsActivity;
import com.example.esbttr.details_activities.MatchDetailsActivity;
import com.example.esbttr.view_activities.ViewBetsActivity;
import com.example.esbttr.view_activities.ViewLeaguesActivity;
import com.example.esbttr.view_activities.ViewMatchesActivity;
import com.example.esbttr.view_activities.ViewTeamsActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    public MaterialToolbar toolbar;
    public MatchesDataBaseHelper matchesDataBaseHelper;
    public TableLayout featuredMatchesTable;

    public MaterialButton viewTeams;
    public MaterialButton viewMatches;
    public MaterialButton viewLeagues;
    public MaterialButton viewBets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();


        viewTeams = (MaterialButton) findViewById(R.id.viewTeams);
        viewLeagues = (MaterialButton) findViewById(R.id.viewLeagues);
        viewMatches = (MaterialButton) findViewById(R.id.viewMatches);
        viewBets = (MaterialButton) findViewById(R.id.viewBets);
        featuredMatchesTable = (TableLayout) findViewById(R.id.featuredMatchesTable);

        matchesDataBaseHelper = new MatchesDataBaseHelper(this);
        //Setup featured matches
        fillFeaturedMatchesTableRandom();

        //Profile View
        toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent object to start the new activity
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);

                // Pass data to the new activity (optional)
                intent.putExtra("key", "value");
                String username = getIntent().getExtras().getString("username");
                intent.putExtra("username", username);
                // Start the new activity
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
            }
        });

        viewTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent object to start the new activity
                Intent intent = new Intent(HomeActivity.this, ViewTeamsActivity.class);
                intent.putExtra("username", getIntent().getExtras().getString("username"));
                // Start the new activity
                startActivity(intent);
            }
        });

        viewMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent object to start the new activity
                Intent intent = new Intent(HomeActivity.this, ViewMatchesActivity.class);
                intent.putExtra("username", getIntent().getExtras().getString("username"));
                // Start the new activity
                startActivity(intent);
            }
        });

        viewBets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent object to start the new activity
                Intent intent = new Intent(HomeActivity.this, ViewBetsActivity.class);
                intent.putExtra("username", getIntent().getExtras().getString("username"));
                // Start the new activity
                startActivity(intent);
            }
        });

        viewLeagues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent object to start the new activity
                Intent intent = new Intent(HomeActivity.this, ViewLeaguesActivity.class);
                intent.putExtra("username", getIntent().getExtras().getString("username"));
                // Start the new activity
                startActivity(intent);
            }
        });
    }

    private void fillFeaturedMatchesTableRandom() {
        //Get all matches
        List<Match> matches = matchesDataBaseHelper.getAllMatches();
        //Get a random number between 1 and matches.length
        int randomMatchSize = (int) (Math.random() * matches.size());
        //Add a random match to the featured matches table randomMatchSize times
        for(int i=0; i<randomMatchSize; i++){
            //Get a random match
            Match randomMatch = matches.get((int) (Math.random() * matches.size()));

            //Add the random match to the featured matches table
            addMatchToTable(randomMatch);

            //Remove the random match from the matches list
            matches.remove(randomMatch);
        }
    }

    private void addMatchToTable(Match currMatch){
        TextView textView = new TextView(this);
        textView.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));
        textView.setText(currMatch.toString());
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Create an Intent object to start the new activity
                Intent intent = new Intent(HomeActivity.this, MatchDetailsActivity.class);

                // Pass data to the new activity (optional)
                intent.putExtra("homeTeam", currMatch.getHome());
                intent.putExtra("awayTeam", currMatch.getAway());
                intent.putExtra("leagueName", currMatch.getLeague());
                intent.putExtra("isFinished", currMatch.isFinished());
                intent.putExtra("username", intent.getExtras().getString("username"));

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

        featuredMatchesTable.addView(textView);
    }

    @Override
    public void onBackPressed() {
        // Do nothing (i.e. disable the back button)
    }
}