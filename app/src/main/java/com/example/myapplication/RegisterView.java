package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.format.DateTimeFormatter;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class RegisterView extends AppCompatActivity {

    private EditText userName,userEmail, userPassword, confirmPassword, dateOfBirth;
    private Button registerButton;

    private TextView loginLink;
    private ProgressBar registerProgressBar;
    private Intent Registerintent;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_view);

        userName = findViewById(R.id.register_name);
        userEmail = findViewById(R.id.register_email);
        userPassword = findViewById(R.id.register_password);
        confirmPassword = findViewById(R.id.register_confirm_password);
        dateOfBirth = findViewById(R.id.register_DOB);

        registerButton = findViewById(R.id.register_btn);

        loginLink = findViewById(R.id.text_login);

        Registerintent = new Intent(this, MainActivity.class);

        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://animal-sound-userinfo-default-rtdb.firebaseio.com/");

        addListeners();



    }

    private void addListeners(){
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = userName.getText().toString();
                String email = userEmail.getText().toString();
                String password = userPassword.getText().toString();
                String confirmpassword = confirmPassword.getText().toString();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                String dateString = dateOfBirth.getText().toString();





                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmpassword.isEmpty()){
                    showMessage("Invalid input");
                }else if (!password.equals(confirmpassword)){
                    showMessage("Different Password! ");
                }else if(!isValidDate(dateString,"yyyy/MM/dd")){
                    showMessage("Wrong date!");

                } else if(hasSpecialSymbols(name)){
                    showMessage("Name contains special symbol");

                }else if (!hasOnlyOneAtSymbol(email)) {
                    showMessage("Wrong email");

                }else if (password.length()<6){
                    showMessage("Password need to be longer than 6 characters");
                }
                else{

                    User user = new User(name,email, password,dateString);

                    //Connect with uid
                    // After successful input validation, create a user in Firebase Authentication
                    firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // User creation successful
                                        FirebaseUser registeredUser = firebaseAuth.getCurrentUser();
                                        String userUid = registeredUser.getUid(); // Get the UID

                                        // Create a User object with UID
                                        User userWithUid = new User(user.getName(), user.getEmail(), user.getPassword(), user.getDate_of_birth(), userUid);

                                        // Save the user data to the Realtime Database under the UID
                                        databaseReference.child("Users").child(userUid).setValue(userWithUid, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                if (databaseError != null) {
                                                    // Error handling
                                                    Log.e("Firebase", "Data could not be saved. " + databaseError.getMessage());
                                                } else {
                                                    // Data was successfully saved
                                                    Log.i("Firebase", "Data saved successfully.");
                                                    showMessage("Successfully registered");

                                                    // Now, you can navigate to the desired activity
                                                    directToLoginpage();
                                                }
                                            }
                                        });

                                    } else {
                                        showMessage(task.getException().toString());
                                    }
                                }
                            });




                }
                
            }

            private void directToLoginpage(){
                Intent directto_login = new Intent(getApplicationContext(), LoginView.class);
                startActivity(directto_login);
                finish();
            }




        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent directtoLogin = new Intent(getApplicationContext(),LoginView.class);
                startActivity(directtoLogin);
                finish();

            }
        });
    }

    private void showMessage(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }



    public boolean isValidDate(String inputDate, String format) {
        // Define a regular expression pattern for the expected date format
        String pattern = "^[0-9]{4}/[0-9]{2}/[0-9]{2}$";

        if (!Pattern.matches(pattern, inputDate)) {
            return false; // Date format doesn't match the expected pattern
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate date;

        try {
            date = LocalDate.parse(inputDate, formatter);
        } catch (Exception e) {

            return false; // Parsing failed, return false
        }

        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        if (year < 1 || month < 1 || month > 12 || day < 1) {
            return false; // Year, month, or day out of valid range
        }

        // Check the day for the specific month
        if (month == 2) { // February
            if (year % 4 == 0) { // Leap year
                return day <= 29;
            } else { // Non-leap year
                return day <= 28;
            }
        } else if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
            return false; // April, June, September, November has 30 days max
        } else if (day > 31) {
            return false; // All other months have 31 days max
        }

        return true; // Date is valid
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

    public static boolean hasOnlyOneAtSymbol(String input){
        int num_of_At = 0;
        for(char c: input.toCharArray()){
            if (c == '@'){
                num_of_At++;
            }
        }
        if (num_of_At == 1){
            return true;
        }else{
            return false;
        }
    }
}