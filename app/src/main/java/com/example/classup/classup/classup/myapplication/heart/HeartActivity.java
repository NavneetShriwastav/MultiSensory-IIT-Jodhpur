package com.example.classup.classup.classup.myapplication.heart;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.classup.classup.classup.myapplication.MainActivity;
import com.example.classup.classup.classup.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class HeartActivity extends AppCompatActivity implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int MEASUREMENT_DURATION_MS = 5000;
    private static final int MIN_BPM = 60;
    private static final int MAX_BPM = 100;

    private SurfaceView surfaceView;
    private TextView bpmText;
    private Button startButton;
    private Camera camera;
    private SurfaceHolder surfaceHolder;
    private boolean isMeasuring = false;
    private List<Integer> ppgData = new ArrayList<>();
    private CountDownTimer countDownTimer;
    private TextView timerText;

    private ImageView iv;

    private Button check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart);

        surfaceView = findViewById(R.id.surfaceView);
        bpmText = findViewById(R.id.bpmText);
        startButton = findViewById(R.id.startButton);
        timerText = findViewById(R.id.timerText);
        check = findViewById(R.id.check);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        startButton.setOnClickListener(v -> {
            if (!isMeasuring) {
                startMeasuring();
            } else {
                stopMeasuring();
            }
        });

        iv = findViewById(R.id.arrowBack);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });



        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the BPM text from bpmText TextView
                String bpmString = bpmText.getText().toString();

                // Check if bpmString contains a valid BPM value
                if (bpmString.startsWith("BPM: ")) {
                    // Extract the BPM value and parse it
                    int measuredBPM = Integer.parseInt(bpmString.substring(5));
                    // Start BPMAnalysisActivity with BPM value
                    Intent intent = new Intent(HeartActivity.this, BPMAnalysisActivity.class);
                    intent.putExtra("bpm", measuredBPM);
                    startActivity(intent);
                } else {
                    // Handle case where BPM text is not in the expected format
                    Toast.makeText(HeartActivity.this, "Please first measure your BPM", Toast.LENGTH_SHORT).show();
                }
            }
        });



        requestCameraPermission();
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            setupCamera();
        }
    }

    private void setupCamera() {
        try {
            camera = Camera.open();
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startMeasuring() {
        if (camera != null) {
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.setPreviewCallback(this);
                camera.startPreview();
                isMeasuring = true;
                ppgData.clear();
                startButton.setText("Stop Measuring");
                startTimer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void stopMeasuring() {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            isMeasuring = false;
            startButton.setText("Start Measuring");
            stopTimer();
            calculateAndDisplayBPM();
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(MEASUREMENT_DURATION_MS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsLeft = millisUntilFinished / 1000;
                timerText.setText("Time Left: " + secondsLeft + "s");
            }

            @Override
            public void onFinish() {
                timerText.setText("Time Left: 0s");
                stopMeasuring(); // Stop measuring automatically after the timer finishes
            }
        }.start();
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        if (camera != null) {
            try {
                camera.setPreviewDisplay(holder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        if (camera != null && isMeasuring) {
            camera.stopPreview();
            try {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        if (camera != null) {
            stopMeasuring();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (isMeasuring) {
            // Implement PPG signal processing algorithm here to calculate BPM
            int bpm = calculateBPMFromPPGData(data);
            if (bpm >= MIN_BPM && bpm <= MAX_BPM) {
                ppgData.add(bpm);
            }
        }
    }

    private void calculateAndDisplayBPM() {
        // Calculate the average BPM from the PPG data
        int sum = 0;
        for (int bpm : ppgData) {
            sum += bpm;
        }
        int averageBpm = sum / ppgData.size();

        // Display the average BPM
        bpmText.setText("BPM: " + averageBpm);
    }



    // Method to calculate BPM from PPG data (replace this with your actual implementation)
    private int calculateBPMFromPPGData(byte[] data) {
        // Your implementation to calculate BPM from PPG data
        // This is a placeholder method, replace it with actual BPM calculation logic
        return MIN_BPM + (int) (Math.random() * (MAX_BPM - MIN_BPM + 1));
    }


}
