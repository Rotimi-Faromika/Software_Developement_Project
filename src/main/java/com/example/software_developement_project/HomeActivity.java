package com.example.software_developement_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import io.paperdb.Paper;

@SuppressWarnings("FieldCanBeLocal")
public class HomeActivity extends AppCompatActivity {

    private Button LogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        LogOut = findViewById(R.id.logout_action);
         LogOut.setOnClickListener(v -> {
             Paper.book().destroy();
             Intent intent = new Intent(HomeActivity.this, LogChoice.class);
             startActivity(intent);
         });
    }
}