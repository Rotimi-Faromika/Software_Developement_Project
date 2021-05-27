package com.example.software_development_project;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.software_development_project.Model.Users;
import com.example.software_development_project.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private EditText inputNumber;
    private EditText inputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private TextView AdminLink, NotAdminLink;

    private String parentDb = "Users";
    private CheckBox chkBox;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = findViewById(R.id.login_btn);
        inputNumber = findViewById(R.id.phone_number);
        inputPassword = findViewById(R.id.password);
        loadingBar = new ProgressDialog(this);
        AdminLink = findViewById(R.id.admin);
        NotAdminLink = findViewById(R.id.not_admin);
        chkBox = findViewById(R.id.remember_me);
        Paper.init(this);

        LoginButton.setOnClickListener(v -> LoginUser());
        AdminLink.setOnClickListener(v -> {
            LoginButton.setText("Login Admin");
            AdminLink.setVisibility(View.INVISIBLE);
            NotAdminLink.setVisibility(View.VISIBLE);
            parentDb = "Admins";
        });
        NotAdminLink.setOnClickListener(v -> {
            LoginButton.setText("Login");
            AdminLink.setVisibility(View.VISIBLE);
            NotAdminLink.setVisibility(View.INVISIBLE);
            parentDb = "Users";
        });
    }

    private void LoginUser(){
        String phone = inputNumber.getText().toString();
        String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please Provide a Phone Number..", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Create a Password..", Toast.LENGTH_SHORT).show();

        } else{
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please Wait, While we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone, password);
        }
    }

    private void AllowAccessToAccount(String phone, String password){

        if(chkBox.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.userPasswordKey, password);
        }
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDb).child(phone).exists()){
                    Users usersData = snapshot.child(parentDb).child(phone).getValue(Users.class);
                    assert usersData != null;
                    if (usersData.getPhone().equals(phone)){
                        if(usersData.getPassword().equals(password)){
                           if(parentDb.equals("Admins")){
                               Toast.makeText(LoginActivity.this, "Welcome Admin, You are " +
                                               "being Logged " +
                                               "in..",
                                       Toast.LENGTH_SHORT).show();
                               loadingBar.dismiss();

                               Intent intent = new Intent(LoginActivity.this, AdminCategory.class);
                               startActivity(intent);
                           } else if(parentDb.equals("Users")){
                               Toast.makeText(LoginActivity.this, "Logged in Successfully..",
                                       Toast.LENGTH_SHORT).show();
                               loadingBar.dismiss();

                               Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                               Prevalent.currentOnlineUser = usersData;
                               startActivity(intent);
                           }
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password is Incorrect.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                } else{
                    Toast.makeText(LoginActivity.this, "Account with this" + phone + "number do " +
                                    "not exists.",
                            Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}