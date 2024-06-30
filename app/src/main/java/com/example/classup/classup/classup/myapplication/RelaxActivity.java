package com.example.classup.classup.classup.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.classup.classup.classup.myapplication.ambient.AmbientActivity;
import com.example.classup.classup.classup.myapplication.binaural.BinauralActivity;
import com.example.classup.classup.classup.myapplication.classic.ClassicActivity;
import com.example.classup.classup.classup.myapplication.guidedmed.GuidedActivity;
import com.example.classup.classup.classup.myapplication.relax.NatureActivity;
import com.example.classup.classup.classup.myapplication.vibration.VibActivity;

public class RelaxActivity extends AppCompatActivity {

    ImageView arrowBack;
    CardView natureCv,classicCv,ambientCv,binauralCv,guidedCv,vibCv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relax);

        arrowBack = findViewById(R.id.arrowBack);

        natureCv = findViewById(R.id.natureCv);

        classicCv = findViewById(R.id.classicCv);

        ambientCv = findViewById(R.id.ambientCv);
        binauralCv = findViewById(R.id.binauralCv);
        guidedCv = findViewById(R.id.guidedCv);
        vibCv = findViewById(R.id.vibCv);



        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RelaxActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        natureCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RelaxActivity.this, NatureActivity.class);
                startActivity(intent);
            }
        });

        classicCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RelaxActivity.this, ClassicActivity.class);
                startActivity(intent);
            }
        });

        ambientCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RelaxActivity.this, AmbientActivity.class);
                startActivity(intent);
            }
        });
        binauralCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RelaxActivity.this, BinauralActivity.class);
                startActivity(intent);
            }
        });
        guidedCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RelaxActivity.this, GuidedActivity.class);
                startActivity(intent);
            }
        });
        vibCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RelaxActivity.this, VibActivity.class);
                startActivity(intent);
            }
        });

    }
}
