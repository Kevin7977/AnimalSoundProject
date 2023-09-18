package com.example.myapplication;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.view.View;
import android.widget.*;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityLoginViewBinding;

public class LoginView extends AppCompatActivity {

    private EditText emailInput,passwordInput;
    private Button loginBtn;
    private TextView registerLink;
    private ProgressBar loginProgressBar;
    private Intent homeintent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);

        emailInput = findViewById(R.id.login_email);
        passwordInput = findViewById(R.id.login_password);

        registerLink = findViewById(R.id.text_register);
        loginProgressBar = findViewById(R.id.login_progress);

        loginBtn = findViewById(R.id.btn_login);

        homeintent = new Intent(this, MainActivity.class);

        addListeners();



    }

    private void addListeners(){
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //testing

                showMessage("Hi !!!!!!!!!!!!!!");
            }
        });
    }

    private void showMessage(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

}