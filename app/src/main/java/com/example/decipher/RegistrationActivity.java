package com.example.decipher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {
    ImageButton register;
    EditText registrationEmail,registrationPassword,firstNameReg,lastNameReg,nativeReg;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        register = findViewById(R.id.registerButton);
        registrationEmail = findViewById(R.id.registrationEmail);
        registrationPassword = findViewById(R.id.registrationPassword);
        firstNameReg = findViewById(R.id.firstNameReg);
        lastNameReg = findViewById(R.id.lastNameReg);
        nativeReg = findViewById(R.id.nativeReg);
        db = new DatabaseHelper(this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.registration(registrationEmail.getText().toString(),registrationPassword.getText().toString(),firstNameReg.getText().toString()
                ,lastNameReg.getText().toString(),nativeReg.getText().toString())) {
                    Intent i = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Unable to Register",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}