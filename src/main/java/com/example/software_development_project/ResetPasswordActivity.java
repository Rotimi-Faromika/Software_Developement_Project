package com.example.software_development_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.software_development_project.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class ResetPasswordActivity extends AppCompatActivity {
    private String check = "";
    private TextView pageTitle, titleQuestions;
    private EditText phoneNumber, question1, question2;
    private Button verifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check = getIntent().getStringExtra("check");
        pageTitle = findViewById(R.id.reset_password);
        titleQuestions = findViewById(R.id.title_questions);
        phoneNumber = findViewById(R.id.find_phone_number);
        question1 = findViewById(R.id.questions_1);
        question2 = findViewById(R.id.questions_2);
        verifyButton = findViewById(R.id.verify_button);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();
        phoneNumber.setVisibility(View.GONE);
        if(check.equals("settings")){
            pageTitle.setText("Set Questions");
            titleQuestions.setText("Please Set Answers for the Following Security Questions");
            verifyButton.setText("Set");

            displayPreviousAnswers();

            verifyButton.setOnClickListener(view -> setAnswers());

        }else if (check.equals("login")){
            phoneNumber.setVisibility(View.VISIBLE);
            verifyButton.setOnClickListener(view -> verifyUser());
        }
    }

    protected void setAnswers(){
        String answer1 = question1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();

        //noinspection EqualsBetweenInconvertibleTypes
        if(question1.equals("") && question2.equals("")){
            Toast.makeText(ResetPasswordActivity.this, "Please answer both questions.",
                    Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(
                    "Users").child(Prevalent.currentOnlineUser.getPhone());
            HashMap<String, Object> userData = new HashMap<>();
            userData.put("answer1", answer1);
            userData.put("answer2", answer2);

            ref.child("Security Questions").updateChildren(userData).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(ResetPasswordActivity.this, "you have set the" +
                            " security questions successfully.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ResetPasswordActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            });
        }

    }

    private void displayPreviousAnswers(){
        DatabaseReference ref =
                FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String ans1 = Objects.requireNonNull(snapshot.child("answer1").getValue()).toString();
                    String ans2 = Objects.requireNonNull(snapshot.child("answer2").getValue()).toString();

                    question1.setText(ans1);
                    question2.setText(ans2);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void verifyUser(){
        String phone = phoneNumber.getText().toString();
        String answer1 = question1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();

        if(!phone.equals("") && !answer1.equals("") && !answer2.equals("")){
            final DatabaseReference ref =
                    FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        //noinspection unused
                        String mPhone = Objects.requireNonNull(snapshot.child("phone").getValue()).toString();
                        if(snapshot.hasChild("Security Questions")){
                            String ans1 =
                                    Objects.requireNonNull(snapshot.child("Security Questions").child("answer1").getValue()).toString();
                            String ans2 = Objects.requireNonNull(snapshot.child("Security " +
                                    "Questions").child("answer2").getValue()).toString();
                            if (!ans1.equals(answer1)){
                                Toast.makeText(ResetPasswordActivity.this, "your 1st answer is " +
                                        "wrong", Toast.LENGTH_SHORT).show();
                            } else if(!ans2.equals(answer2)){
                                Toast.makeText(ResetPasswordActivity.this, "your 2nd answer is " +
                                        "wrong", Toast.LENGTH_SHORT).show();
                            } else{
                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");
                                final EditText newPassword =
                                        new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Write Password Here..");
                                builder.setView(newPassword);

                                builder.setPositiveButton("Change", (dialog, which) -> {
                                    if(!newPassword.getText().toString().equals("")){
                                        ref.child("password").setValue(newPassword.getText().toString()).addOnCompleteListener(task -> {
                                            if (task.isSuccessful()){
                                                Toast.makeText(ResetPasswordActivity.this,
                                                        "Password changed successfully.",
                                                        Toast.LENGTH_SHORT).show();
                                                Intent intent =
                                                        new Intent(ResetPasswordActivity.this,
                                                                LoginActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                                builder.show();
                            }
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, "you have not set the security" +
                                            " questions.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else{
                        Toast.makeText(ResetPasswordActivity.this, "This phone number does not " +
                                "exist.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        } else{
            Toast.makeText(this, "please complete the form.", Toast.LENGTH_SHORT).show();
        }
    }
}