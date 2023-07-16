package com.example.software_development_project.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.software_development_project.HomeActivity;
import com.example.software_development_project.LogChoice;
import com.example.software_development_project.R;

@SuppressWarnings("FieldCanBeLocal")
public class AdminCategoryActivity extends AppCompatActivity {
    private ImageView tShirts, Dresses, Trousers;
    private ImageView Sweaters, kidsWear, Shirts;
    private ImageView Hoodies;

    private Button LogoutBtn, CheckOrdersBtn, maintainProductsBtn;

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
        LogoutBtn = findViewById(R.id.admin_logout_orders);
        CheckOrdersBtn = findViewById(R.id.check_orders);
        maintainProductsBtn = findViewById(R.id.maintain);

        maintainProductsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCategoryActivity.this, HomeActivity.class);
            intent.putExtra("Admin", "Admin");
            startActivity(intent);
        });

        LogoutBtn.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCategoryActivity.this, LogChoice.class);
            //noinspection PointlessBitwiseExpression
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        CheckOrdersBtn.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminNewOrdersActivity.class);
            startActivity(intent);
            finish();
        });

        tShirts.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
            intent.putExtra("Category", "T Shirts");
            startActivity(intent);
        });
        Dresses.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
            intent.putExtra("Category", "Dresses");
            startActivity(intent);
        });
        Trousers.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
            intent.putExtra("Category", "Trousers");
            startActivity(intent);
        });
        Sweaters.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
            intent.putExtra("Category", "Sweaters");
            startActivity(intent);
        });
        kidsWear.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
            intent.putExtra("Category", "Kids Wear");
            startActivity(intent);
        });
        Shirts.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
            intent.putExtra("Category", "Shirts");
            startActivity(intent);
        });
        Hoodies.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminProductActivity.class);
            intent.putExtra("Category", "Hoodies");
            startActivity(intent);
        });
    }
}