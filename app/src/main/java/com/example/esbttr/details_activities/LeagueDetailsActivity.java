package com.example.esbttr.details_activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.esbttr.ProfileActivity;
import com.example.esbttr.R;
import com.example.esbttr.add_activities.AddMatchActivity;
import com.example.esbttr.classes.League;
import com.example.esbttr.classes.Match;
import com.example.esbttr.databases.LeaguesDataBaseHelper;
import com.example.esbttr.databases.MatchesDataBaseHelper;
import com.example.esbttr.view_activities.ViewMatchesActivity;
import com.example.esbttr.view_activities.ViewTeamsActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class LeagueDetailsActivity extends AppCompatActivity {

    private EditText leagueName;
    private MaterialButton updateLeagueBtn;
    private MaterialButton deleteLeagueBtn;
    private MaterialButton addMatchToLeague;
    private TableLayout matchesTable;
    private String leagueNameString;
    private LeaguesDataBaseHelper leaguesDataBaseHelper;
    private MatchesDataBaseHelper matchesDataBaseHelper;
    private List<Match> matchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_details);
        getSupportActionBar().hide();

        leagueName = (EditText) findViewById(R.id.leagueName);

        matchesTable = (TableLayout) findViewById(R.id.table);

        leaguesDataBaseHelper = new LeaguesDataBaseHelper(this);
        matchesDataBaseHelper = new MatchesDataBaseHelper(this);

        Intent intent = getIntent();
        leagueNameString = intent.getStringExtra("leagueName");
        leagueName.setText(leagueNameString);

        updateLeagueBtn = findViewById(R.id.updateLeagueBtn);
        deleteLeagueBtn = findViewById(R.id.deleteLeagueBtn);
        addMatchToLeague = findViewById(R.id.addMatchToLeagueBtn);

        checkAdmin();

        //Profile View
        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent object to start the new activity
                Intent intent = new Intent(LeagueDetailsActivity.this, ProfileActivity.class);

                // Pass data to the new activity (optional)
                intent.putExtra("key", "value");
                String username = getIntent().getExtras().getString("username");
                intent.putExtra("username", username);
                // Start the new activity
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
            }
        });

        updateLeagueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Implement Update League Function
                String newLeagueName = leagueName.getText().toString();
                leaguesDataBaseHelper.updateLeague(leagueNameString, newLeagueName);
            }
        });

        deleteLeagueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Implement Delete League Function
                leaguesDataBaseHelper.deleteLeague(leagueNameString);
                //Delete all matches from this league
                matchesDataBaseHelper.deleteAllMatchesFromLeague(leagueNameString);
                finish();
            }
        });

        addMatchToLeague.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent object to start the new activity
                Intent intent = new Intent(LeagueDetailsActivity.this, AddMatchActivity.class);

                // Pass data to the new activity (optional)
                intent.putExtra("leagueName", leagueNameString);

                // Start the new activity
                startActivity(intent);
            }
        });

    }

    private void checkAdmin() {
        String username = getIntent().getExtras().getString("username");
        if(!username.equals("admin")){
            updateLeagueBtn.setVisibility(View.GONE);
            deleteLeagueBtn.setVisibility(View.GONE);
            addMatchToLeague.setVisibility(View.GONE);
            leagueName.setClickable(false);
            leagueName.setFocusable(false);
        }
    }

    private void addMatchToTable(Match currMatch){
        TextView textView = new TextView(this);
        textView.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));
        textView.setText(currMatch.getHome() + " vs " + currMatch.getAway());
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Create an Intent object to start the new activity
                Intent intent = new Intent(LeagueDetailsActivity.this, MatchDetailsActivity.class);

                // Pass data to the new activity (optional)
                intent.putExtra("homeTeam", currMatch.getHome());
                intent.putExtra("awayTeam", currMatch.getAway());
                intent.putExtra("leagueName", currMatch.getLeague());
                intent.putExtra("isFinished", currMatch.isFinished());
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

        matchesTable.addView(textView);
    }

    //On resume, update the table
    @Override
    protected void onResume() {
        super.onResume();
        matchList = matchesDataBaseHelper.getAllMatchesFromLeague(leagueNameString);
        matchesTable.removeAllViews();

        for (int i = 0; i < matchList.size(); i++) {
            if(!matchList.get(i).isFinished())
                addMatchToTable(matchList.get(i));
        }
    }
}