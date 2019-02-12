package com.mheleon.mycapmquiz.controllers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mheleon.mycapmquiz.R;
import com.mheleon.mycapmquiz.models.CheckAnswer;

import java.util.List;

public class CheckAnswerAdapter extends RecyclerView.Adapter<CheckAnswerAdapter.MyViewHolder> {
    private List<CheckAnswer> answerList;

    /**
     * Provide a suitable constructor
     *
     * @param answerList
     */
    public CheckAnswerAdapter(List<CheckAnswer> answerList) {
        this.answerList = answerList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CheckAnswer checkAnswer = answerList.get(position);
        holder.nbQuestion.setText(checkAnswer.getNbQuestion());
        holder.question.setText(checkAnswer.getQuestion());
        holder.userAnswer.setText(checkAnswer.getUserAnswer());
        holder.correctAnswer.setText(checkAnswer.getCorrectAnswer());
        holder.explanation.setText(checkAnswer.getExplanation());
    }

    /**
     * Method that gets size of the dataset (invoked by the layout manager)
     *
     * @return Return the size of the dataset
     */
    @Override
    public int getItemCount() {
        return answerList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checkanswer, parent, false);
        return new MyViewHolder(v);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView nbQuestion;
        public TextView question;
        public TextView userAnswer;
        public TextView correctAnswer;
        public TextView explanation;

        public MyViewHolder(View view) {
            super(view);
            nbQuestion = (TextView) view.findViewById(R.id.nbQuestion);
            question = (TextView) view.findViewById(R.id.question);
            userAnswer = (TextView) view.findViewById(R.id.userAnswer);
            correctAnswer = (TextView) view.findViewById(R.id.correctAnswer);
            explanation = (TextView) view.findViewById(R.id.explanation);
        }
    }
}
