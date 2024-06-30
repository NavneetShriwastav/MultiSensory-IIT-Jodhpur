package com.example.classup.classup.classup.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.classup.classup.classup.myapplication.heart.HeartActivity;
import com.example.classup.classup.classup.myapplication.questions.QuestionnaireActivity;

public class MainActivity extends AppCompatActivity {
    CardView relax,heart,question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relax = findViewById(R.id.relax);
        heart = findViewById(R.id.heart);
        question = findViewById(R.id.question);
        relax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RelaxActivity.class);
                startActivity(intent);
            }
        });
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HeartActivity.class);
                startActivity(intent);
            }
        });
        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, QuestionnaireActivity.class);
                startActivity(intent);
            }
        });

    }
}
