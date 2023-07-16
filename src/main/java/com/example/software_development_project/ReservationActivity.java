package com.example.software_development_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.software_development_project.Admin.AdminUserProductsActivity;
import com.example.software_development_project.Model.Reservations;
import com.example.software_development_project.Prevalent.Prevalent;
import com.example.software_development_project.ViewHolder.ReservationViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

@SuppressWarnings("FieldCanBeLocal")
public class ReservationActivity extends AppCompatActivity {
    private EditText nameEditText, phoneEditText;
    private Button reserveOrder;
    private RecyclerView reservationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        nameEditText = findViewById(R.id.reservation_name);
        phoneEditText = findViewById(R.id.reservation_phone_number);
        reserveOrder = findViewById(R.id.confirm_final_order);
        reservationList = findViewById(R.id.reservation_list);
        reservationList.setLayoutManager(new LinearLayoutManager(this));
        reserveOrder.setOnClickListener(v -> Check());
    }

    private void Check() {
        if (TextUtils.isEmpty(nameEditText.getText().toString())) {
            Toast.makeText(this, "Please Provide your Full Name.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phoneEditText.getText().toString())) {
            Toast.makeText(this, "Please Provide your Phone Number", Toast.LENGTH_SHORT).show();
        } else {
            reservationList.setLayoutManager(new LinearLayoutManager(this));
            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {
        final String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy", Locale.UK);
        saveCurrentDate = currentDate.format(calForDate.getTime());

        //noinspection unused
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a", Locale.UK);
        saveCurrentTime = currentDate.format(calForDate.getTime());

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child(
                "Reservations").child(Prevalent.currentOnlineUser.getPhone());
        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("name", nameEditText.getText().toString());
        ordersMap.put("phone", phoneEditText.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);

        FirebaseRecyclerOptions<Reservations> options =
                new FirebaseRecyclerOptions.Builder<Reservations>().setQuery(ordersRef
                                .child(Prevalent.currentOnlineUser.getPhone()).child("Products"),
                        Reservations.class).build();

        FirebaseRecyclerAdapter<Reservations, ReservationViewHolder> adapter =
                new FirebaseRecyclerAdapter<Reservations, ReservationViewHolder>(options) {

            @NonNull
            @Override
            public ReservationViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view =
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_items_layout, parent,
                                true);
                return new ReservationViewHolder(view);
            }

            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull @NotNull ReservationViewHolder holder,
                                            int position, @NonNull  Reservations model) {
                holder.txtUserName.setText("User Name: " +  model.getName());
                holder.txtUserPhone.setText("Phone Number: " + model.getPhone());
                holder.orderDate.setText("Order at: " + model.getDate() + " " + model.getTime());

                holder.showReservationsButton.setOnClickListener(view -> {
                    String uID = getRef(position).getKey();
                    Intent intent = new Intent(ReservationActivity.this,
                            AdminUserProductsActivity.class);
                    intent.putExtra("uid", uID);
                    startActivity(intent);
                });

                holder.itemView.setOnClickListener(view -> {
                    CharSequence[] options = new CharSequence[]{

                            "Edit",
                            "Remove"
                    };
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(ReservationActivity.this);
                    builder.setTitle("Reservation Options:");
                    builder.setItems(options, (dialog, which) -> {
                        if (which == 0) {
                            Intent intent = new Intent(ReservationActivity.this,
                                    ProductDetailsActivity.class);
                            intent.putExtra("pid", model.getPid());
                            startActivity(intent);
                        }
                        if (which == 1) {
                            ordersRef.child(Prevalent.currentOnlineUser.getPhone()).child(
                                    "Products").child(model.getPid()).removeValue().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ReservationActivity.this, "Item Removed " +
                                                    "Successfully.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ReservationActivity.this,
                                            HomeActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                    builder.show();
                });
            }
        };
        ordersRef.updateChildren(ordersMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseDatabase.getInstance().getReference().child("Reservations").child(Prevalent.currentOnlineUser.getPhone()).removeValue().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(ReservationActivity.this, "Reservation Successfully " +
                                        "Placed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Intent intent = new Intent(ReservationActivity.this,HomeActivity.class);
                //noinspection PointlessBitwiseExpression
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        reservationList.setAdapter(adapter);
        adapter.startListening();
    }
}