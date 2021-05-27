package com.example.software_development_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

@SuppressWarnings("FieldCanBeLocal")
public class AdminCategory extends AppCompatActivity {
    private ImageView tShirts, Dresses, Trousers;
    private ImageView Sweaters, kidsWear, Shirts;
    private ImageView Hoodies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        tShirts = findViewById(R.id.t_shirts);
        Dresses = findViewById(R.id.dresses);
        Trousers = findViewById(R.id.trousers);
        Sweaters = findViewById(R.id.sweaters);
        kidsWear = findViewById(R.id.kidswear);
        Shirts = findViewById(R.id.shirts);
        Hoodies = findViewById(R.id.hoodies);

        tShirts.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategory.this, AdminProductActivity.class);
            intent.putExtra("Category", "T Shirts");
            startActivity(intent);
        });
        Dresses.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategory.this, AdminProductActivity.class);
            intent.putExtra("Category", "Dresses");
            startActivity(intent);
        });
        Trousers.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategory.this, AdminProductActivity.class);
            intent.putExtra("Category", "Trousers");
            startActivity(intent);
        });
        Sweaters.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategory.this, AdminProductActivity.class);
            intent.putExtra("Category", "Sweaters");
            startActivity(intent);
        });
        kidsWear.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategory.this, AdminProductActivity.class);
            intent.putExtra("Category", "Kids Wear");
            startActivity(intent);
        });
        Shirts.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategory.this, AdminProductActivity.class);
            intent.putExtra("Category", "Shirts");
            startActivity(intent);
        });
        Hoodies.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategory.this, AdminProductActivity.class);
            intent.putExtra("Category", "Hoodies");
            startActivity(intent);
        });
    }
}