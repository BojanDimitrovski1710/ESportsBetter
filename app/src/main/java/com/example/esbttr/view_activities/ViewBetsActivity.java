package com.example.esbttr.view_activities;

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
import com.example.esbttr.classes.Bet;
import com.example.esbttr.databases.BetsDatabaseHelper;
import com.example.esbttr.databases.MatchesDataBaseHelper;
import com.example.esbttr.details_activities.MatchDetailsActivity;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

public class ViewBetsActivity extends AppCompatActivity {
    public BetsDatabaseHelper betsDb;
    public TableLayout betsTable;
    public List<Bet> userBets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bets);
        getSupportActionBar().hide();

        betsDb = new BetsDatabaseHelper(this);
        betsTable = (TableLayout) findViewById(R.id.betsTable);
        userBets = betsDb.getBetsForUser(getIntent().getExtras().getString("username"));

        for (int i = 0; i < userBets.size(); i++) {
            addBetToTable(userBets.get(i));
        }

        //Profile View
        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent object to start the new activity
                Intent intent = new Intent(ViewBetsActivity.this, ProfileActivity.class);

                // Pass data to the new activity (optional)
                String username = getIntent().getExtras().getString("username");
                intent.putExtra("username", username);
                // Start the new activity
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
            }
        });


    }

    private void addBetToTable(Bet bet) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));
        textView.setText(bet.getMatch() + " | " + bet.getWager() + "$");
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

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

        betsTable.addView(textView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        userBets = betsDb.getBetsForUser(getIntent().getExtras().getString("username"));
        betsTable.removeAllViews();

        for (int i = 0; i < userBets.size(); i++) {
            addBetToTable(userBets.get(i));
        }
    }
}