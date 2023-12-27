package com.gov.doitc.genericfacerd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SpleshNextActivity extends AppCompatActivity {
    Button kiosk,beneficiary,login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splesh_next);
        kiosk = findViewById(R.id.btn_kiosk);
        beneficiary = findViewById(R.id.btn_beneficary);
        login = findViewById(R.id.btn_login);
        kiosk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SpleshNextActivity.this,EmitraKioskRegisterActivity.class);
                startActivity(i);

            }
        });
        beneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SpleshNextActivity.this,BeneficiaryRegistrationActivity.class);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SpleshNextActivity.this,LogInActivity.class);
                startActivity(i);
            }
        });
    }
}