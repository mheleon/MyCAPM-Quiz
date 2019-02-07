package com.mheleon.mycapmquiz;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CheckAnswerAdapter extends RecyclerView.Adapter<CheckAnswerAdapter.MyViewHolder> {
    private List<CheckAnswer> answerList;

    // private String[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        // public TextView mTextView;
        public TextView nbQuestion;
        public TextView question;
        public TextView userAnswer;
        public TextView correctAnswer;

        public MyViewHolder(View view) {
            super(view);
            // mTextView = v;
            nbQuestion = (TextView) view.findViewById(R.id.nbQuestion);
            question = (TextView) view.findViewById(R.id.question);
            userAnswer = (TextView) view.findViewById(R.id.userAnswer);
            correctAnswer = (TextView) view.findViewById(R.id.correctAnswer);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CheckAnswerAdapter(List<CheckAnswer> answerList) {
        this.answerList = answerList;
    }
/*
    // Create new views (invoked by the layout manager)
    @Override
    public CheckAnswerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);
        ...
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }
*/
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        // holder.mTextView.setText(mDataset[position]);

        CheckAnswer checkAnswer = answerList.get(position);
        holder.nbQuestion.setText(checkAnswer.getNbQuestion());
        holder.question.setText(checkAnswer.getQuestion());
        holder.userAnswer.setText(checkAnswer.getUserAnswer());
        holder.correctAnswer.setText(checkAnswer.getCorrectAnswer());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return answerList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checkanswer,parent, false);
        return new MyViewHolder(v);
    }
}
