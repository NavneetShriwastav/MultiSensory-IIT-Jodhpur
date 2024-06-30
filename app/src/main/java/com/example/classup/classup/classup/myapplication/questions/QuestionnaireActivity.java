package com.example.classup.classup.classup.myapplication.questions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.classup.classup.classup.myapplication.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionnaireActivity extends AppCompatActivity {

    private List<Question> questions;
    private int currentQuestionIndex = 0;

    private TextView questionText;
    private ImageView questionImage;
    private RadioGroup optionsGroup;
    private Button nextButton;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        questionText = findViewById(R.id.question_text);
        questionImage = findViewById(R.id.question_image);
        optionsGroup = findViewById(R.id.options_group);
        nextButton = findViewById(R.id.next_button);
        backButton = findViewById(R.id.back);

        loadQuestions();
        displayQuestion();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optionsGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(QuestionnaireActivity.this, "Please select an option before proceeding", Toast.LENGTH_SHORT).show();
                } else {
                    saveAnswer();
                    if (currentQuestionIndex < questions.size() - 1) {
                        currentQuestionIndex++;
                        displayQuestion();
                    } else {
                        int[] answers = new int[5];
                        for (int i = 0; i < questions.size(); i++) {
                            answers[i] = questions.get(i).getSelectedOption();
                        }

                        // Determine the state of mind
                        String mindState = getMindState(answers);

                        // Pass answers and mind state to MediaActivity
                        Intent intent = new Intent(QuestionnaireActivity.this, MediaActivity.class);
                        intent.putExtra("ANSWERS", answers);  // Pass the answers as an int array
                        intent.putExtra("MIND_STATE", mindState);
                        startActivity(intent);
                    }
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex--;
                    displayQuestion();
                } else {
                    finish();
                }
            }
        });

        // Initially disable the next button
        nextButton.setEnabled(false);

        // Listen for changes in the RadioGroup to enable the next button
        optionsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                nextButton.setEnabled(true);  // Enable the next button when an option is selected
            }
        });
    }

    private void loadQuestions() {
        questions = new ArrayList<>();
        questions.add(new Question("How are you feeling right now?", Arrays.asList("Happy", "Sad", "Angry", "Anxious")));
        questions.add(new Question("How was your day?", Arrays.asList("Great", "Okay", "Bad", "Terrible")));
        questions.add(new Question("How much stress do you feel?", Arrays.asList("None", "Some", "A lot", "Overwhelming")));
        questions.add(new Question("How is your energy level?", Arrays.asList("High", "Normal", "Low", "Very Low")));
        questions.add(new Question("How are you sleeping?", Arrays.asList("Well", "Okay", "Poorly", "Very Poorly")));
    }

    private void displayQuestion() {
        Question currentQuestion = questions.get(currentQuestionIndex);
        questionText.setText(currentQuestion.getQuestionText());

        int imageResId = getResources().getIdentifier(getImageNameForQuestion(currentQuestionIndex), "drawable", getPackageName());
        questionImage.setImageResource(imageResId);

        // Set the background color based on the current question
        setBackgroundColorForQuestion(currentQuestionIndex);

        optionsGroup.removeAllViews();
        for (int i = 0; i < currentQuestion.getOptions().size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(currentQuestion.getOptions().get(i));
            radioButton.setId(i);
            optionsGroup.addView(radioButton);

            // Set the checked state based on the saved answer
            if (i == currentQuestion.getSelectedOption()) {
                radioButton.setChecked(true);
            }
        }

        // Update the `nextButton` based on whether an option is selected
        nextButton.setEnabled(optionsGroup.getCheckedRadioButtonId() != -1);

        // Set the `OnCheckedChangeListener` to manage the button state
        optionsGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // When an option is selected, enable the next button
            nextButton.setEnabled(true);
        });
    }

    private String getImageNameForQuestion(int questionIndex) {
        switch (questionIndex) {
            case 0:
                return "feeling";
            case 1:
                return "day";
            case 2:
                return "stress";
            case 3:
                return "energy";
            case 4:
                return "sleeping";
            default:
                return "default_image"; // A default image if the index is out of range
        }
    }

    private void saveAnswer() {
        int selectedId = optionsGroup.getCheckedRadioButtonId();
        questions.get(currentQuestionIndex).setSelectedOption(selectedId);
    }

    private String getMindState(int[] answers) {
        int positiveCount = 0;
        int neutralCount = 0;
        int negativeCount = 0;

        // Check responses for each question to count how many are in each state
        if (answers[0] == 0) positiveCount++; // Feeling Happy
        if (answers[1] == 0) positiveCount++; // Day Great
        if (answers[2] == 0) positiveCount++; // Stress None
        if (answers[3] == 0) positiveCount++; // Energy High
        if (answers[4] == 0) positiveCount++; // Sleeping Well

        if (answers[0] == 1 || answers[0] == 2 || answers[0] == 3) negativeCount++; // Feeling Sad, Angry, Anxious
        if (answers[1] == 2 || answers[1] == 3) negativeCount++; // Day Bad, Terrible
        if (answers[2] == 1 || answers[2] == 2 || answers[2] == 3) negativeCount++; // Stress Some, A lot, Overwhelming
        if (answers[3] == 2 || answers[3] == 3) negativeCount++; // Energy Low, Very Low
        if (answers[4] == 2 || answers[4] == 3) negativeCount++; // Sleeping Poorly, Very Poorly

        // Determine the overall mind state
        if (positiveCount >= 3) {
            return "Positive";
        } else if (negativeCount >= 3) {
            return "Negative";
        } else {
            return "Neutral";
        }
    }

    private void setBackgroundColorForQuestion(int questionIndex) {
        // Define color codes
        int[] colors = {
                getResources().getColor(R.color.happy_color), // Happy
                getResources().getColor(R.color.day_color),   // Day
                getResources().getColor(R.color.stress_color), // Stress
                getResources().getColor(R.color.energy_color), // Energy
                getResources().getColor(R.color.sleeping_color) // Sleeping
        };

        // Set the background color based on the current question
        findViewById(R.id.background).setBackgroundColor(colors[questionIndex]);
    }
}
