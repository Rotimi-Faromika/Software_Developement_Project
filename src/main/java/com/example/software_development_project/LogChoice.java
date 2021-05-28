package com.example.software_development_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
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

import io.paperdb.Paper;

@SuppressWarnings("FieldCanBeLocal")
public class LogChoice extends AppCompatActivity {
    private Button registerButton, loginButton, homePage;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_choice);

        registerButton = findViewById(R.id.register_btn);
        loginButton = findViewById(R.id.login_btn);
        homePage = findViewById(R.id.homepage);
        loadingBar = new ProgressDialog(this);

        Paper.init(this);

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

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.userPasswordKey);

        if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)){
            AllowAccess(UserPhoneKey, UserPasswordKey);

            loadingBar.setTitle("Logged In.");
            loadingBar.setMessage("Please Wait....");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
        }

    }

    private void AllowAccess(final String phone, final String password){
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Users").child(phone).exists()){
                    Users usersData = snapshot.child("Users").child(phone).getValue(Users.class);
                    assert usersData != null;
                    if (usersData.getPhone().equals(phone)){
                        if(usersData.getPassword().equals(password)){
                            Toast.makeText(LogChoice.this, "Logged in Successfully..",
                                    Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(LogChoice.this, HomeActivity.class);
                            Prevalent.currentOnlineUser = usersData;
                            startActivity(intent);
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(LogChoice.this, "Password is Incorrect.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                } else{
                    Toast.makeText(LogChoice.this, "Account with this" + phone + "number do " +
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
    public void onBackPressed(){
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
