package com.example.software_development_project;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {
    private Button paymentButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        paymentButton = findViewById(R.id.pay_button);
    }
}