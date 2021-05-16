package com.example.software_developement_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LogChoice extends AppCompatActivity {
    private Button registerButton, loginButton, homePage;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_choice);

        registerButton = findViewById(R.id.register_btn);
        loginButton = findViewById(R.id.login_btn);
        homePage = findViewById(R.id.homepage);

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(LogChoice.this, LoginActivity.class);
            startActivity(intent);
        });
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LogChoice.this, RegisterActivity.class);
            startActivity(intent);
        });
        homePage.setOnClickListener(v -> {
            Intent intent = new Intent(LogChoice.this, HomeActivity.class);
            startActivity(intent);
        });

    }
    public void onBackPressed(){
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
