package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuestActivity extends AppCompatActivity {
    private EditText userPhone,userEmail;
    private Button loginBtn;
    private TextView GoBackTextView;
    private CheckBox check18Box;
    private DatabaseReference firebaseDataReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        userPhone = findViewById(R.id.guest_inputbox_phone);
        userEmail = findViewById(R.id.guest_inputbox_email);

        loginBtn = findViewById(R.id.guest_login_btn);

        GoBackTextView = findViewById(R.id.text_guest_Back);

        check18Box = findViewById(R.id.guest_age_checkbox);
        firebaseDataReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://animal-sound-userinfo-default-rtdb.firebaseio.com/");

        addListeners();




    }

    private void addListeners(){
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = userPhone.getText().toString();
                String email = userEmail.getText().toString();

                boolean Over18 = check18Box.isChecked();

                if (phone.isEmpty() && email.isEmpty()){
                    showMessage("Need a phone number or email");
                }else if(!email.isEmpty() && !hasOnlyOneAtSymbol(email)){
                    showMessage("Wrong email");
                }else if (!phone.isEmpty()&& !isValidPhoneNumber(phone)){
                    showMessage("Wrong phone number");
                }else if(Over18 == false){
                    showMessage("Need to be over 18 years old");
                }else{
                    direct_to_home();
                }
            }
        });

        GoBackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent directtoLogin = new Intent(getApplicationContext(),LoginView.class);
                startActivity(directtoLogin);
                finish();

            }
        });

    }
    private static boolean hasOnlyOneAtSymbol(String input){
        int num_of_At_symbol = 0;
        for(char c: input.toCharArray()){
            if (c == '@'){
                num_of_At_symbol++;
            }
        }
        if (num_of_At_symbol == 1){
            return true;
        }else{
            return false;
        }
    }

    private void showMessage(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }
    public static boolean isValidPhoneNumber(String phoneNumber) {
        // Define the regular expression for a valid phone number
        String regex = "^(\\+[0-9]{1,3})?\\s?\\(?[0-9]{3}\\)?[-.\\s]?[0-9]{3}[-.\\s]?[0-9]{4}$";

        // Compile the regular expression
        Pattern pattern = Pattern.compile(regex);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(phoneNumber);

        // Check if the phone number matches the pattern
        return matcher.matches();
    }

    private void direct_to_home(){
        showMessage("Successfully login");
        //direct to homepage
        Intent directto_Home = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(directto_Home);
        finish();
    }

}