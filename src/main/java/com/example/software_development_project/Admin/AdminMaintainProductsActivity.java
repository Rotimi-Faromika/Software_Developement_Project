package com.example.software_development_project.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.software_development_project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

public class AdminMaintainProductsActivity extends AppCompatActivity {
    @SuppressWarnings("FieldCanBeLocal")
    private Button applyChanges, deleteBtn;
    private EditText name, price, description;
    private ImageView imageView;
    private String productID = "";
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        productID = getIntent().getStringExtra("pid");
        productsRef =
                FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        applyChanges = findViewById(R.id.apply_changes);
        name = findViewById(R.id.product_name_maintain);
        price = findViewById(R.id.product_price_maintain);
        description = findViewById(R.id.product_description_maintain);
        imageView = findViewById(R.id.product_image_maintain);
        deleteBtn = findViewById(R.id.delete_product);
        
        displaySpecificProductInfo();

        applyChanges.setOnClickListener(view -> applyChanges());

        deleteBtn.setOnClickListener(view -> deleteThisProduct());
    }

    private void deleteThisProduct() {
        productsRef.removeValue().addOnCompleteListener(task -> {
            Intent intent = new Intent(AdminMaintainProductsActivity.this,
                    AdminCategoryActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(AdminMaintainProductsActivity.this, "The Product was deleted " +
                    "successfully.", Toast.LENGTH_SHORT).show();
        });
    }

    private void applyChanges() {
        String pName = name.getText().toString();
        String pPrice = price.getText().toString();
        String pDescription = description.getText().toString();

        if(pName.equals("")){
            Toast.makeText(this, "Write down Product Name", Toast.LENGTH_SHORT).show();
        } else if (pPrice.equals("")){
            Toast.makeText(this, "Write down Product Price", Toast.LENGTH_SHORT).show();
        } else if (pDescription.equals("")){
            Toast.makeText(this, "Write down Product Description", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productID);
            productMap.put("description", pDescription);
            productMap.put("price", pPrice);
            productMap.put("pname", pName);

            productsRef.updateChildren(productMap).addOnCompleteListener(task -> {
                 if(task.isSuccessful()){
                     Toast.makeText(AdminMaintainProductsActivity.this, "Changes applied " +
                             "successfully.", Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent(AdminMaintainProductsActivity.this,
                             AdminCategoryActivity.class);
                     startActivity(intent);
                     finish();
                 }
            });
        }
    }

    private void displaySpecificProductInfo() {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String pName = Objects.requireNonNull(snapshot.child("pname").getValue()).toString();
                    String pPrice = Objects.requireNonNull(snapshot.child("price").getValue()).toString();
                    String pDescription = Objects.requireNonNull(snapshot.child("description").getValue()).toString();
                    String pImage = Objects.requireNonNull(snapshot.child("image").getValue()).toString();

                    name.setText(pName);
                    price.setText(pPrice);
                    description.setText(pDescription);
                    Picasso.get().load(pImage).fit().into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}