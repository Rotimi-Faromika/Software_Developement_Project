package com.example.software_developement_project;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

@SuppressWarnings("FieldCanBeLocal")
public class AdminProductActivity extends AppCompatActivity {

    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);


       categoryName = getIntent().getExtras().get("Category").toString();

        Toast.makeText(this, categoryName, Toast.LENGTH_SHORT).show();
    }
}