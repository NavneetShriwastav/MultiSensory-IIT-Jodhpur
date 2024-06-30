package com.example.classup.classup.classup.myapplication.questions;

import java.util.List;

public class Question {
    private String questionText;
    private List<String> options;
    private int selectedOption = -1;

    public Question(String questionText, List<String> options) {
        this.questionText = questionText;
        this.options = options;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(int selectedOption) {
        this.selectedOption = selectedOption;
    }
}
