package com.example.classup.classup.classup.myapplication.heart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.classup.classup.classup.myapplication.R;

public class MaintainHeartActivity extends AppCompatActivity {
    TextView bpmValue;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain_heart);
        bpmValue = findViewById(R.id.bpmValue);
        back = findViewById(R.id.arrowBack);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MaintainHeartActivity.this,BPMAnalysisActivity.class);
                startActivity(intent);
            }
        });


        // Inside onCreate() method or any other appropriate place in the receiving activity
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("bpm")) {
            int data = intent.getIntExtra("bpm", 0);
            bpmValue.setText(String.valueOf(data));
        }

    }
}
