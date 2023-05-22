package com.example.esbttr.add_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.esbttr.R;
import com.example.esbttr.classes.Bet;
import com.example.esbttr.databases.BetsDatabaseHelper;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class AddBetActivity extends AppCompatActivity {

    private Spinner predictionSpinner;
    private EditText wagerEditText;
    private MaterialButton finishBetButton;
    private BetsDatabaseHelper betsDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bet);
        predictionSpinner = findViewById(R.id.predictionSpinner);
        wagerEditText = findViewById(R.id.wagerAmount);
        finishBetButton = findViewById(R.id.finishBetButton);
        Intent intent = getIntent();

        ArrayList<String> predictions = new ArrayList<>();
        predictions.add(intent.getStringExtra("homeTeamName"));
        predictions.add(intent.getStringExtra("awayTeamName"));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, predictions);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        predictionSpinner.setAdapter(arrayAdapter);
        predictionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String prediction = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + prediction, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        finishBetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = intent.getStringExtra("username");
                String prediction = predictionSpinner.getSelectedItem().toString();
                int wager = Integer.parseInt(wagerEditText.getText().toString());
                String match = intent.getStringExtra("homeTeamName") + " vs " + intent.getStringExtra("awayTeamName");
                Bet bet = new Bet(username, match, wager, prediction);

                betsDatabaseHelper.addBet(bet);
            }
        });

    }

}