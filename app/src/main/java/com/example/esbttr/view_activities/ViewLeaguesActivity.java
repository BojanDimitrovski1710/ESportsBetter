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
import android.widget.Toast;

import com.example.esbttr.ProfileActivity;
import com.example.esbttr.R;
import com.example.esbttr.add_activities.AddLeagueActivity;
import com.example.esbttr.classes.League;
import com.example.esbttr.databases.LeaguesDataBaseHelper;
import com.example.esbttr.details_activities.LeagueDetailsActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ViewLeaguesActivity extends AppCompatActivity {

    private LeaguesDataBaseHelper leaguesDb;
    private TableLayout leaguesTable;
    private MaterialButton addLeagueButton;
    private List<League> leagueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_leagues);
        getSupportActionBar().hide();

        leaguesDb = new LeaguesDataBaseHelper(this);
        leaguesTable = (TableLayout) findViewById(R.id.leaguesTable);
        addLeagueButton = (MaterialButton) findViewById(R.id.addLeagueButton);
        leagueList = leaguesDb.getAllLeauges();

        for(int i=0; i<leagueList.size(); i++){
            League currLeague = leagueList.get(i);
            addLeaguetoTable(currLeague);
        }

        checkAdmin();

        //Profile View
        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent object to start the new activity
                Intent intent = new Intent(ViewLeaguesActivity.this, ProfileActivity.class);

                // Pass data to the new activity (optional)
                intent.putExtra("key", "value");
                String username = getIntent().getExtras().getString("username");
                intent.putExtra("username", username);
                // Start the new activity
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
            }
        });

        addLeagueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent object to start the new activity
                Intent intent = new Intent(ViewLeaguesActivity.this, AddLeagueActivity.class);

                // Start the new activity
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
            }
        });
    }

    private void checkAdmin() {
        String username = getIntent().getExtras().getString("username");
        if(!username.equals("admin")){
            addLeagueButton.setVisibility(View.INVISIBLE);

        }
    }

    private void addLeaguetoTable(League currLeague){
        TextView textView = new TextView(this);
        textView.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));
        textView.setText(currLeague.getName());
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                // Create an Intent object to start the new activity
                Intent intent = new Intent(ViewLeaguesActivity.this, LeagueDetailsActivity.class);

                // Pass data to the new activity (optional)
                intent.putExtra("leagueName", currLeague.getName());
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

        leaguesTable.addView(textView);
    }

    //On Resume, update the table
    @Override
    protected void onResume() {
        super.onResume();

        //Clear the table
        leaguesTable.removeAllViews();

        //Get the new list of leagues
        leagueList = leaguesDb.getAllLeauges();

        //Add the leagues to the table
        for(int i=0; i<leagueList.size(); i++){
            League currLeague = leagueList.get(i);
            addLeaguetoTable(currLeague);
        }
    }
}