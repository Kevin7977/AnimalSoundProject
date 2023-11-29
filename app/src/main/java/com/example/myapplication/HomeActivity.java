package com.example.myapplication;



import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private static final String CAT_SOUND = "MEOW";
    private static final String DOG_SOUND = "WOOF";
    private static final String COW_SOUND = "MOO";
    private static final String DUCK_SOUND = "QUACK";
    private String UserUid ="";

    private ImageView ImageView;

    private ActivityResultLauncher<Intent> speechRecognitionLauncher;
    private TextView resultText;
    private ImageButton voiceButton;
    private Button EditProfileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        resultText = findViewById(R.id.textTv);
        voiceButton = findViewById(R.id.voiceBtn);

        ImageView = findViewById(R.id.Img_home);
        EditProfileBtn = findViewById(R.id.EditProfileBtn);

        UserUid = getIntent().getStringExtra("UserUid");
        if (UserUid.isEmpty()){
            EditProfileBtn.setEnabled(false);
        }







        speechRecognitionLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        handleSpeechRecognitionResult(result);
                    }
                }
        );

        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSpeechRecognition();
            }
        });

        EditProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent directto_EditProfile = new Intent(getApplicationContext(), EditProfileActivity.class);
                directto_EditProfile.putExtra("UserUid",UserUid);
                startActivity(directto_EditProfile);
                finish();
            }
        });
    }

    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Try to make a animal sound");

        speechRecognitionLauncher.launch(intent);
    }

    private void handleSpeechRecognitionResult(ActivityResult result) {
        int resultCode = result.getResultCode();
        Intent data = result.getData();



        /*if (resultCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> resultSpeech = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            displayResult("gg");
            if (resultSpeech.size() > 0) {
                String recognizedText = resultSpeech.get(0);
                displayResult(recognizedText);


                if (recognizedText.toLowerCase().contains(CAT_SOUND.toLowerCase())) {
                    displayResult("Cat");
                } else if (recognizedText.toLowerCase().contains(DOG_SOUND.toLowerCase())) {
                    displayResult("Dog");
                } else if (recognizedText.toLowerCase().contains(COW_SOUND.toLowerCase())) {
                    displayResult("Cow");
                } else {
                    displayResult("Unrecognized Sound");
                }
            }
        }*/
        ArrayList<String> resultSpeech = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        if( !resultSpeech.get(0).isEmpty()){
            int cat_distance = compute_Levenshtein_distanceDP(CAT_SOUND,resultSpeech.get(0));
            int dog_distance = compute_Levenshtein_distanceDP(DOG_SOUND,resultSpeech.get(0));
            int cow_distance = compute_Levenshtein_distanceDP(COW_SOUND,resultSpeech.get(0));
            int quack_distance = compute_Levenshtein_distanceDP(DUCK_SOUND,resultSpeech.get(0));

            //displayResult(resultSpeech.get(0)+ cat_distance +dog_distance+cow_distance);
            if(cat_distance <= 1){
                displayResult("It's a cat");
                ImageView.setImageDrawable(getDrawable(R.drawable.cat));

            }else if(dog_distance <= 1){
                displayResult("It's a dog");
                ImageView.setImageDrawable(getDrawable(R.drawable.dog));

            }else if (cow_distance <= 1){
                displayResult("It's a cow");
                ImageView.setImageDrawable(getDrawable(R.drawable.cow));

            }else if(quack_distance <= 1){
                displayResult("It's a duck");
                ImageView.setImageDrawable(getDrawable(R.drawable.duck));

            }else {
                displayResult("Unknown voice");
                ImageView.setImageDrawable(getDrawable(R.drawable.question_mark));
            }

        }

    }

    private void displayResult(String result) {
        resultText.setText(result);
    }


    private static int compute_Levenshtein_distanceDP(String str1,
                                              String str2)
    {

        // A 2-D matrix to store previously calculated
        // answers of subproblems in order
        // to obtain the final
        str1 = str1.toLowerCase();
        str2 = str2.toLowerCase();

        int[][] dp = new int[str1.length() + 1][str2.length() + 1];

        for (int i = 0; i <= str1.length(); i++)
        {
            for (int j = 0; j <= str2.length(); j++) {

                // If str1 is empty, all characters of
                // str2 are inserted into str1, which is of
                // the only possible method of conversion
                // with minimum operations.
                if (i == 0) {
                    dp[i][j] = j;
                }

                // If str2 is empty, all characters of str1
                // are removed, which is the only possible
                // method of conversion with minimum
                // operations.
                else if (j == 0) {
                    dp[i][j] = i;
                }

                else {
                    // find the minimum among three
                    // operations below


                    dp[i][j] = minm_edits(dp[i - 1][j - 1]
                                    + NumOfReplacement(str1.charAt(i - 1),str2.charAt(j - 1)), // replace
                            dp[i - 1][j] + 1, // delete
                            dp[i][j - 1] + 1); // insert
                }
            }
        }

        return dp[str1.length()][str2.length()];
    }

    // check for distinct characters
    // in str1 and str2

    static int NumOfReplacement(char c1, char c2)
    {
        return c1 == c2 ? 0 : 1;
    }

    // receives the count of different
    // operations performed and returns the
    // minimum value among them.

    private static int minm_edits(int... nums)
    {

        return Arrays.stream(nums).min().orElse(
                Integer.MAX_VALUE);
    }

}