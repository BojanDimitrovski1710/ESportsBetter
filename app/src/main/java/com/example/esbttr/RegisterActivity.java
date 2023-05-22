package com.example.esbttr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.esbttr.classes.User;
import com.example.esbttr.databases.UsersDatabaseHelper;
import com.google.android.material.button.MaterialButton;

import java.io.Console;

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText email;
    private EditText password;
    private EditText passwordConfirm;
    private UsersDatabaseHelper userDatabaseHelper;

    private MaterialButton registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        userDatabaseHelper = new UsersDatabaseHelper(this);

        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        passwordConfirm = (EditText) findViewById(R.id.password_confrirm);
        registerButton = (MaterialButton) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validFields = validFields();

                if(validFields == true){
                    User currUser = new User(username.getText().toString(), email.getText().toString(), password.getText().toString(), false, 1000);
                    if(userDatabaseHelper.isValidEntry(currUser)){
                        userDatabaseHelper.addUser(currUser);
                        Toast.makeText(RegisterActivity.this, "Successful Registration", Toast.LENGTH_SHORT).show();
                        userDatabaseHelper.close();
                        finish();
                    }else{
                        Toast.makeText(RegisterActivity.this, "Username already in use", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "Invalid Registration", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validFields(){
        if(username.getText().toString() == null){
            System.out.println("1");
            Toast.makeText(RegisterActivity.this, username.getText().toString(), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(email.getText().toString() == null){
            System.out.println("2");
            Toast.makeText(RegisterActivity.this, email.getText().toString(), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.getText().toString() == null){
            System.out.println("3");
            Toast.makeText(RegisterActivity.this, password.getText().toString(), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(passwordConfirm.getText().toString() == null){
            System.out.println("4");
            Toast.makeText(RegisterActivity.this, passwordConfirm.getText().toString(), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!(password.getText().toString().equals(passwordConfirm.getText().toString()))){
            System.out.println("5");
            Toast.makeText(RegisterActivity.this, "Passwords Dont match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}