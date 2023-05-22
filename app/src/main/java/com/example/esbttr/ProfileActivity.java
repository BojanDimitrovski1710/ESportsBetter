package com.example.esbttr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.esbttr.classes.User;
import com.example.esbttr.databases.UsersDatabaseHelper;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URI;

public class ProfileActivity extends AppCompatActivity {

    MaterialButton logOutButton;
    TextView username;
    TextView funds;
    ImageView profilePicture;
    private FirebaseAuth firebaseAuth;
    private MaterialButton chooseImageBtn;
    private MaterialButton takePictureBtn;

    private UsersDatabaseHelper usersDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        username = (TextView) findViewById(R.id.username);
        funds = (TextView) findViewById(R.id.funds);
        profilePicture = (ImageView) findViewById(R.id.profilePicture);
        chooseImageBtn = (MaterialButton) findViewById(R.id.chooseImage);
        takePictureBtn = (MaterialButton) findViewById(R.id.takePicture);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        usersDatabaseHelper = new UsersDatabaseHelper(this);

        if(user != null){
            username.setText(user.getDisplayName());
            Uri uri = user.getPhotoUrl();
            usersDatabaseHelper.updateImage(user.getDisplayName(), uri.toString());
            Picasso.get().load(uri).into(profilePicture);
        }else{
            username.setText(getIntent().getExtras().getString("username"));
            String uri = usersDatabaseHelper.getImage(getIntent().getExtras().getString("username"));
            if(uri != null){
                Uri imageUri = Uri.parse(uri);
                Picasso.get().load(imageUri).into(profilePicture);
            }
            else{
                Toast.makeText(this, "Please select a profile picture", Toast.LENGTH_SHORT).show();
            }
        }

        funds.setText("Funds: " + usersDatabaseHelper.getUserFunds(getIntent().getExtras().getString("username")) + " $");

        logOutButton = (MaterialButton) findViewById(R.id.logOut);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();

                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        chooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //On click, open the gallery and return the picture chosen
                ImagePicker.Companion.with(ProfileActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start(101);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            Uri filePath = data.getData();
            profilePicture.setImageURI(filePath);
            usersDatabaseHelper.updateImage(username.getText().toString(), filePath.toString());

        }
    }


}