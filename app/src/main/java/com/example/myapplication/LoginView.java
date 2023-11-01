package com.example.myapplication;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginView extends AppCompatActivity {

    private EditText emailInput,passwordInput;
    private Button loginBtn,guestloginBtn;
    private TextView registerLink;
    private ProgressBar loginProgressBar;
    private Intent homeintent;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDataReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);

        emailInput = findViewById(R.id.login_email);
        passwordInput = findViewById(R.id.login_password);

        registerLink = findViewById(R.id.text_register);
        loginProgressBar = findViewById(R.id.login_progress);

        loginBtn = findViewById(R.id.btn_login);
        guestloginBtn = findViewById(R.id.Login_guest_login_btn);

        homeintent = new Intent(this, MainActivity.class);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDataReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://animal-sound-userinfo-default-rtdb.firebaseio.com/");

        addListeners();



    }

    private void addListeners(){
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //testing


                String emailValue = emailInput.getText().toString();
                String passwordValue = passwordInput.getText().toString();
                showProgressBar();

                if (emailValue.isEmpty() || passwordValue.isEmpty()){
                    showMessage("Email or Password is Empty!");
                    hideProgressBar();
                }else{
                    login(emailValue,passwordValue);
                }



            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent directtoRegister = new Intent(getApplicationContext(),RegisterView.class);
                startActivity(directtoRegister);
                finish();

            }
        });

        guestloginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent directto_Guestlogin = new Intent(getApplicationContext(), GuestActivity.class);
                startActivity(directto_Guestlogin);
                finish();

            }
        });

    }

    private void showMessage(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    private void login(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            hideProgressBar();
                            //direct to voice page
                            showMessage("Successful login in ");
                            Intent directto_Home = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(directto_Home);
                            finish();



                        }else{
                            String errormsg = task.getException().toString();

                            showMessage(errormsg);
                            hideProgressBar();
                        }
                    }
                });
        firebaseDataReference = FirebaseDatabase.getInstance().getReference("Users");

        firebaseDataReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<User> users = new ArrayList<User>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class); // Assuming you have a User class
                        //showMessage("Found user: " + user.toString());
                        users.add(user);
                    }
                    //default to user 1 that has the same the email and password solution. Could be modify.
                    if (!users.isEmpty()) {
                        User login_user = users.get(0);
                        DatabaseReference modifyDataReference = FirebaseDatabase.getInstance().getReference();
                        modifyDataReference.child("CurrentUser").setValue(login_user);
                        //System.out.println(login_user.toString());
                    }

                } else {
                    System.out.println("User not found.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Database error: " + databaseError.getMessage());
            }
        });

    }

    private void showProgressBar(){
        loginBtn.setVisibility(View.INVISIBLE);
        guestloginBtn.setVisibility(View.INVISIBLE);
        loginProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar(){
        loginBtn.setVisibility(View.VISIBLE);
        guestloginBtn.setVisibility(View.VISIBLE);
        loginProgressBar.setVisibility(View.INVISIBLE);
    }

}