package com.example.software_development_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    @SuppressWarnings("FieldCanBeLocal")
    private Button regButton;
    private EditText Name, PhoneNumber, Password;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regButton = findViewById(R.id.register_action);
        Name = findViewById(R.id.register_username);
        PhoneNumber = findViewById(R.id.register_phone);
        Password = findViewById(R.id.register_password);
        loadingBar = new ProgressDialog(this);

        regButton.setOnClickListener(v -> CreateAccount());
    }

    private void CreateAccount(){
        String name = Name.getText().toString();
        String phone = PhoneNumber.getText().toString();
        String password = Password.getText().toString();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please Provide a Name..", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please Provide a Phone Number..", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Create a Password..", Toast.LENGTH_SHORT).show();

        } else{
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please Wait, While we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhoneNumber(name, phone, password);
        }
    }

    private void ValidatePhoneNumber(String name, String phone, String password){
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Users").child(phone).exists())){
                    HashMap<String, Object> userData = new HashMap<>();
                    userData.put("Phone", phone);
                    userData.put("Password", password);
                    userData.put("Name", name);

                    RootRef.child("Users").child(phone).updateChildren(userData)
                            .addOnCompleteListener(task -> {
                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this, "Congratulations, " +
                                            "Account Created.", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();

                                    Intent intent = new Intent(RegisterActivity.this, LogChoice.class);
                                    startActivity(intent);
                                } else{
                                    loadingBar.dismiss();
                                    Toast.makeText(RegisterActivity.this, "Network Error: Please " +
                                                    "Try Again",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    Toast.makeText(RegisterActivity.this, "This" + phone + "already exists.",
                            Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again using another number.",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, LogChoice.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}