package com.mheleon.mycapmquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mheleon.mycapmquiz.models.CapmQA;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class QuestionActivity extends AppCompatActivity {

    @BindView(R.id.questionText) TextView questionText;
    @BindView(R.id.radioButtonA) TextView radioButtonA;
    @BindView(R.id.radioButtonB) TextView radioButtonB;
    @BindView(R.id.radioButtonC) TextView radioButtonC;
    @BindView(R.id.radioButtonD) TextView radioButtonD;
    @BindView(R.id.nextButton) Button nextButton;

    // Number of the question, not the array index
    int questionNumber;
    ArrayList<String> userAnswers = new ArrayList<>();
    ArrayList<Integer> questionsIndex;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent intent = new Intent(QuestionActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        radioGroup = findViewById(R.id.radioGroup);

        Intent i = getIntent();
        questionNumber = i.getExtras().getInt("questionNumber");

        // Get random questions index from last activity
        questionsIndex = i.getIntegerArrayListExtra("questionsIndex");

        // Get user's answers from last activity
        userAnswers = i.getStringArrayListExtra("userAnswers");

        setTitle("Question " + questionNumber);
        setFields();
    }

    /**
     * Method to set all fields
     */
    public void setFields() {

        Realm realm = Realm.getDefaultInstance();
        CapmQA capmQA = realm.where(CapmQA.class).equalTo("id", questionsIndex.get(questionNumber - 1)).findFirst();

        questionText.setText(capmQA.getQuestion());
        radioButtonA.setText("A. " + capmQA.getA());
        radioButtonB.setText("B. " + capmQA.getB());
        radioButtonC.setText("C. " + capmQA.getC());
        radioButtonD.setText("D. " + capmQA.getD());

        realm.close();

    }

    /**
     * Method that manages next button
     */
    @OnClick(R.id.nextButton)
    public void submit() {
        int selectedId = radioGroup.getCheckedRadioButtonId();

        String buttonSelected;

        // find which radioButton is checked by id
        if (selectedId == R.id.radioButtonA) {
            buttonSelected = "A";
            userAnswers.set(questionNumber - 1, "a");
        } else if (selectedId == R.id.radioButtonB) {
            buttonSelected = "B";
            userAnswers.set(questionNumber - 1, "b");
        } else if (selectedId == R.id.radioButtonC) {
            buttonSelected = "C";
            userAnswers.set(questionNumber - 1, "c");
        } else if (selectedId == R.id.radioButtonD) {
            buttonSelected = "D";
            userAnswers.set(questionNumber - 1, "d");
        } else {
            Toast.makeText(getApplicationContext(), "Answer the question before continue", Toast.LENGTH_SHORT).show();
            return;
        }

        if (questionNumber < 10) {
            Intent intent = new Intent(QuestionActivity.this, QuestionActivity.class);

            intent.putExtra("questionNumber", questionNumber + 1);
            intent.putStringArrayListExtra("userAnswers", userAnswers);
            intent.putIntegerArrayListExtra("questionsIndex", questionsIndex);
            startActivity(intent);
        } else {
            Intent intent = new Intent(QuestionActivity.this, ResultsActivity.class);
            intent.putExtra("questionNumber", questionNumber + 1);
            intent.putStringArrayListExtra("userAnswers", userAnswers);
            intent.putIntegerArrayListExtra("questionsIndex", questionsIndex);
            startActivity(intent);
        }
    }

}
