package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {
    private EditText nameEditText, oldPasswordEditText, newPasswordEditText;
    private Button saveButton, cancelButton;
    private TextView emailText;
    private String UserUid;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDataReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        nameEditText = findViewById(R.id.nameEditText);
        emailText = findViewById(R.id.emailText);
        oldPasswordEditText = findViewById(R.id.oldPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);

        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);

        UserUid = getIntent().getStringExtra("UserUid");
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDataReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://animal-sound-userinfo-default-rtdb.firebaseio.com/");


        DatabaseReference Usernode = firebaseDataReference.child("Users").child(UserUid.toString());

        Usernode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve user data
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);

                    // Do something with user data
                    emailText.setText(email);
                    nameEditText.setHint("Name : "+ name);
                } else {
                    // The user does not exist in the database
                    Log.w("TAG", "User not found in the database");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                Log.e("TAG", "Error reading user data", databaseError.toException());
            }
        });


        addListeners();
    }

    private void addListeners(){
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent directto_Home = new Intent(getApplicationContext(), HomeActivity.class);
                directto_Home.putExtra("UserUid",UserUid.toString());
                startActivity(directto_Home);
                finish();

            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();

                String oldPassword = oldPasswordEditText.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();

                DatabaseReference Usernode = firebaseDataReference.child("Users").child(UserUid.toString());
                FirebaseUser AuthUser = firebaseAuth.getCurrentUser();

                Usernode.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Retrieve user data
                            String CurrentName = dataSnapshot.child("name").getValue(String.class);
                            String CurrentEmail = dataSnapshot.child("email").getValue(String.class);
                            String CurrentPassword = dataSnapshot.child("password").getValue(String.class);

                            if (oldPassword.equals(CurrentPassword)){
                                String password;
                                if(!name.isEmpty()){
                                    if (!hasSpecialSymbols(name)){
                                        Usernode.child("name").setValue(name);
                                        showMessage("Successfully update the name");
                                        if(newPassword.isEmpty()){
                                            Intent directto_Home = new Intent(getApplicationContext(), HomeActivity.class);
                                            directto_Home.putExtra("UserUid",UserUid.toString());
                                            startActivity(directto_Home);
                                            finish();
                                        }
                                    }else{
                                        showMessage("Name cannot contain special symbol");
                                    }
                                }



                                if(!newPassword.isEmpty()){
                                    if (newPassword.length() >= 6){
                                        AuthUser.updatePassword(newPassword)
                                                .addOnCompleteListener(task -> {
                                                    if (task.isSuccessful()) {
                                                        // Email update successful
                                                        Log.d("TAG", "User password updated.");
                                                        Usernode.child("password").setValue(newPassword);
                                                        showMessage("Successfully update the password");
                                                        Intent directto_Home = new Intent(getApplicationContext(), HomeActivity.class);
                                                        directto_Home.putExtra("UserUid",UserUid.toString());
                                                        startActivity(directto_Home);
                                                        finish();

                                                    } else {
                                                        // Handle errors
                                                        Exception exception = task.getException();
                                                        Log.w("TAG", "Error updating password: " + exception.getMessage());
                                                    }
                                                });
                                        password = newPassword;


                                    }else{
                                        password = oldPassword;
                                        showMessage("New password is too short");
                                    }
                                } else {
                                    password = oldPassword;
                                }





                            }else{
                                showMessage("Wrong old password!");
                            }

                            // Do something with user data

                        } else {
                            // The user does not exist in the database
                            Log.w("TAG", "User not found in the database");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle errors
                        Log.e("TAG", "Error reading user data", databaseError.toException());
                    }
                });





            }

        });
    }



    private void showMessage(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }
    public static boolean hasSpecialSymbols(String input) {
        // Define a set of special symbols
        String specialSymbols = "!@#$%^&*()_+{}[]:;<>,.?~\\-";

        // Iterate through each character in the input string
        for (char c : input.toCharArray()) {
            if (specialSymbols.indexOf(c) != -1) {
                return true;
            }
        }

        return false;
    }


}