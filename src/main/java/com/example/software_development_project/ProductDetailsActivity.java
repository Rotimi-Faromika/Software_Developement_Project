package com.example.software_development_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.software_development_project.Model.Products;
import com.example.software_development_project.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class ProductDetailsActivity extends AppCompatActivity {

    @SuppressWarnings("FieldCanBeLocal")
    private Button addToCart, reserveButton;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescription, productName;
    private String productID = "", state = "Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pid");

        addToCart = findViewById(R.id.add_to_cart);
        numberButton = findViewById(R.id.number_btn);
        productImage = findViewById(R.id.product_image_details);
        productPrice = findViewById(R.id.product_price_details);
        productDescription = findViewById(R.id.product_description_details);
        productName = findViewById(R.id.product_name_details);
        reserveButton = findViewById(R.id.reserve_product);

        getProductDetails(productID);

        addToCart.setOnClickListener(v -> {

            if(state.equals("Order Placed") || state.equals("Order Shipped")){
                Toast.makeText(this, "you can purchase more items, once your item is confirmed or" +
                                " shipped", Toast.LENGTH_LONG).show();
            } else{
                addingToCartList();
            }
        });
        reserveButton.setOnClickListener(v ->{
            Intent intent = new Intent(ProductDetailsActivity.this, ReservationActivity.class);
            startActivity(intent);
        });
    }

    protected void onStart(){
        super.onStart();
        CheckOrderState();

    }

    private void addingToCartList() {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy", Locale.UK);
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a", Locale.UK);
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child(
                "Cart List");
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("discount", "");

        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child(
                "Products").child(productID).updateChildren(cartMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone()).child(
                        "Products").child(productID).updateChildren(cartMap).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(ProductDetailsActivity.this, "Added to Cart List.",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ProductDetailsActivity.this,
                                HomeActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

    }

    private void getProductDetails(String productID) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child(
                "Products");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Products products = snapshot.getValue(Products.class);
                    assert products != null;
                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).fit().into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void CheckOrderState(){
        DatabaseReference ordersRef;
        ordersRef =
                FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    String shippingState = Objects.requireNonNull(snapshot.child("state").getValue()).toString();

                    if(shippingState.equals("shipped")){
                        state = "Order Shipped";
                    } else if (shippingState.equals("not shipped")){
                        state = "Order Placed";
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}