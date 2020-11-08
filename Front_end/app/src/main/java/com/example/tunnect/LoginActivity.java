package com.example.tunnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    public UserService currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        currUser = new UserService();
        EditText username_text = findViewById(R.id.username_text);
        Button loginBtn = findViewById(R.id.login_button);
        loginBtn.setOnClickListener(view -> {
            username_text.getText();

        });
    }
}