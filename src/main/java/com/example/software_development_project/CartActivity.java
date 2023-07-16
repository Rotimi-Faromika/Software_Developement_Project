package com.example.software_development_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.software_development_project.Model.Cart;
import com.example.software_development_project.Prevalent.Prevalent;
import com.example.software_development_project.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView.LayoutManager layoutManager;
    private Button nextProcess;
    private TextView totalAmount, OrderMessage;
    private int overTotalPrice = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nextProcess = findViewById(R.id.next_process_button);
        totalAmount = findViewById(R.id.total_price);
        OrderMessage = findViewById(R.id.order_message);

        nextProcess.setOnClickListener(view -> {
            //noinspection UnnecessaryCallToStringValueOf
            totalAmount.setText("Total Price = " + String.valueOf(overTotalPrice) + "€");

            Intent intent = new Intent(CartActivity.this, ConfirmOrderActivity.class);
            intent.putExtra("Total Price", String.valueOf(overTotalPrice));
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        CheckOrderState();

        final DatabaseReference cartListRef =
                FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListRef.
                                child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Products"),
                        Cart.class).build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart,
                CartViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.txtProductQuantity.setText("Quantity = " + model.getQuantity());
                holder.txtProductPrice.setText("Price = " + model.getPrice() + "€");
                holder.txtProductName.setText(model.getPname());

                //noinspection UnnecessaryBoxing
                int oneTypeProductPrice =
                        ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());

                overTotalPrice = overTotalPrice + oneTypeProductPrice;

                holder.itemView.setOnClickListener(view -> {
                    CharSequence[] options = new CharSequence[]{

                            "Edit",
                            "Remove"
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                    builder.setTitle("Cart Options:");

                    builder.setItems(options, (dialog, which) -> {
                        if (which == 0) {
                            Intent intent = new Intent(CartActivity.this,
                                    ProductDetailsActivity.class);
                            intent.putExtra("pid", model.getPid());
                            startActivity(intent);
                        }
                        if (which == 1) {
                            cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Products").child(model.getPid()).removeValue().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(CartActivity.this, "Item Removed " +
                                                    "Successfully.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CartActivity.this,
                                            HomeActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                    builder.show();
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                //noinspection UnnecessaryLocalVariable
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void CheckOrderState() {
        DatabaseReference ordersRef;
        ordersRef =
                FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        ordersRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String shippingState = Objects.requireNonNull(snapshot.child("state").getValue()).toString();
                    String userName = Objects.requireNonNull(snapshot.child("name").getValue()).toString();

                    if (shippingState.equals("shipped")) {

                        totalAmount.setText("Dear" + userName + "\n order is shipped successfully.");
                        recyclerView.setVisibility(View.GONE);

                        OrderMessage.setVisibility(View.VISIBLE);
                        OrderMessage.setText("Your Order has been Shipped Successfully, Soon you " +
                                "will receive your order.");
                        nextProcess.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "you can purchase more products, once " +
                                "you have received your first order.", Toast.LENGTH_SHORT).show();

                    } else if (shippingState.equals("not shipped")) {

                        totalAmount.setText("Shipping State = Not Shipped");
                        recyclerView.setVisibility(View.GONE);

                        OrderMessage.setVisibility(View.VISIBLE);
                        nextProcess.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}