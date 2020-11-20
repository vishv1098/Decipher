package com.example.decipher;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ImageButton loginButton;
    TextView register;
    EditText login, password;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        loginButton = (ImageButton) findViewById(R.id.loginButton);
        register = findViewById(R.id.register);
        login = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.authenticator(login.getText().toString(), password.getText().toString()) == true) {
                    Intent i = new Intent(MainActivity.this, SplashScreen.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(i1);
            }
        });
    }
}
