package com.example.classup.classup.classup.myapplication.heart;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.classup.classup.classup.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class BPMAnalysisActivity extends AppCompatActivity {

    private TextView bpmValue, tv;
    private Spinner genderSpinner, activitySpinner;
    private EditText ageInput;
    private Button analyzeButton,chb;
    private ImageView iv;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bpmanalysis);

        // Initialize UI components
        bpmValue = findViewById(R.id.bpmValue);
        genderSpinner = findViewById(R.id.genderSpinner);
        activitySpinner = findViewById(R.id.activitySpinner);
        ageInput = findViewById(R.id.ageInput);
        analyzeButton = findViewById(R.id.analyzeButton);
        tv = findViewById(R.id.tv_analyis);
        iv = findViewById(R.id.arrowBack);
        layout = findViewById(R.id.layout);
        chb = findViewById(R.id.chb);

        // Click listeners
        chb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMaintainHeart();
            }
        });
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHeartActivity();
            }
        });

        // Get BPM value from previous activity
        int measuredBPM = getIntent().getIntExtra("bpm", 0);
        bpmValue.setText(String.valueOf(measuredBPM));

        // Populate spinners with data
        populateSpinners();

        // Analyze BPM on button click
        analyzeButton.setOnClickListener(v -> {
            analyzeBPM();
            hideKeyboard();
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            View view = getCurrentFocus();
            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private void populateSpinners() {
        // Populate gender spinner
        List<String> genderList = new ArrayList<>();
        genderList.add("Male");
        genderList.add("Female");
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genderList);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        // Populate activity spinner
        List<String> activityList = new ArrayList<>();
        activityList.add("Sitting");
        activityList.add("Running");
        activityList.add("Reading");
        // Add more activities as needed
        ArrayAdapter<String> activityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, activityList);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(activityAdapter);
    }

    private void analyzeBPM() {
        // Get user inputs
        String gender = genderSpinner.getSelectedItem().toString();
        String activity = activitySpinner.getSelectedItem().toString();
        String ageText = ageInput.getText().toString();

        // Check if age is provided
        if (ageText.isEmpty()) {
            Toast.makeText(this, "Please enter age", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageText);
        int bpm = Integer.parseInt(bpmValue.getText().toString());

        // Determine the normal range based on age and activity level
        int normalMin;
        int normalMax;

        // Define normal BPM ranges based on age and activity level
        if (age >= 18 && age <= 30) {
            normalMin = 60;
            normalMax = 100;
        } else if (age > 30 && age <= 50) {
            normalMin = 60;
            normalMax = 100;
        } else {
            normalMin = 60;
            normalMax = 100;
        }

        // Analyze BPM and display result in TextView
        String message;
        int backgroundColor;
        if (bpm < normalMin) {
            message = "Your heart rate is too low.";
            backgroundColor = Color.RED; // Red background color
        } else if (bpm > normalMax) {
            message = "Your heart rate is too high.";
            backgroundColor = Color.RED; // Red background color
        } else {
            message = "Your heart rate is normal.";
            backgroundColor = Color.GREEN; // Green background color
        }

        // Display analysis message in TextView
        tv.setText(message);
        layout.setBackgroundColor(backgroundColor); // Set background color of LinearLayout

        // Pass BPM to MaintainHeartActivity if it's high or low
        if (bpm > normalMax || bpm < normalMin) {
            navigateToMaintainHeart();
        }
    }

    private void navigateToMaintainHeart() {

        Intent intent = new Intent(BPMAnalysisActivity.this, MaintainHeartActivity.class);
        intent.putExtra("bpm", Integer.parseInt(bpmValue.getText().toString()));
        startActivity(intent);

    }

    private void navigateToHeartActivity() {
        Intent intent = new Intent(BPMAnalysisActivity.this, HeartActivity.class);
        startActivity(intent);
    }
}
