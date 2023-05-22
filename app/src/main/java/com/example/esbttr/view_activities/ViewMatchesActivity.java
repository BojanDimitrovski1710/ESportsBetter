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

import com.example.esbttr.ProfileActivity;
import com.example.esbttr.R;
import com.example.esbttr.add_activities.AddMatchActivity;
import com.example.esbttr.classes.Match;
import com.example.esbttr.databases.MatchesDataBaseHelper;
import com.example.esbttr.details_activities.MatchDetailsActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ViewMatchesActivity extends AppCompatActivity {

    private MatchesDataBaseHelper matchesDb;
    private TableLayout matchesTable;
    private List<Match> matchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_matches);
        getSupportActionBar().hide();

        matchesDb = new MatchesDataBaseHelper(this);
        matchesTable = (TableLayout) findViewById(R.id.matchesTable);
        matchList = matchesDb.getAllMatches();

        for (int i = 0; i < matchList.size(); i++) {
            addMatchToTable(matchList.get(i));
        }

        //Profile View
        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent object to start the new activity
                Intent intent = new Intent(ViewMatchesActivity.this, ProfileActivity.class);

                // Pass data to the new activity (optional)
                intent.putExtra("key", "value");
                String username = getIntent().getExtras().getString("username");
                intent.putExtra("username", username);
                // Start the new activity
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
            }
        });

    }



    private void addMatchToTable(Match currMatch) {
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
                Intent intent = new Intent(ViewMatchesActivity.this, MatchDetailsActivity.class);

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

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            List<Match> newMatches = matchesDb.getAllMatches();
            addMatchToTable(newMatches.get(newMatches.size() - 1));
        }

    }
    */

    //When the user returns to the activity, the table is updated
    @Override
    protected void onResume() {
        super.onResume();

        matchList = matchesDb.getAllMatches();
        matchesTable.removeAllViews();

        for (int i = 0; i < matchList.size(); i++) {
            addMatchToTable(matchList.get(i));
        }
    }
}