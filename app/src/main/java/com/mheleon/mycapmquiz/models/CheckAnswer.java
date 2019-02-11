package com.mheleon.mycapmquiz.models;

public class CheckAnswer {
    private String nbQuestion;
    private String question;
    private String userAnswer;
    private String correctAnswer;

    public CheckAnswer(String nbQuestion, String question, String userAnswer, String correctAnswer) {
        this.nbQuestion = nbQuestion;
        this.question = question;
        this.userAnswer = userAnswer;
        this.correctAnswer = correctAnswer;
    }

    public String getNbQuestion() {
        return nbQuestion;
    }

    public void setNbQuestion(String nbQuestion) {
        this.nbQuestion = nbQuestion;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
