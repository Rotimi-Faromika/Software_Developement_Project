package com.example.software_developement_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.paperdb.Paper;

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