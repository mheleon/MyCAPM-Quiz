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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class QuestionActivity extends AppCompatActivity {

    @BindView(R.id.questionText) TextView questionText;
    @BindView(R.id.radioButtonA) TextView radioButtonA;
    @BindView(R.id.radioButtonB) TextView radioButtonB;
    @BindView(R.id.radioButtonC) TextView radioButtonC;
    @BindView(R.id.radioButtonD) TextView radioButtonD;
    @BindView(R.id.nextButton) Button nextButton;

    // private RadioGroup radioGroup = findViewById(R.id.radioGroup);

    private RadioGroup radioGroup;
    /*
    private RadioButton bA = findViewById(R.id.radioButtonA);
    private RadioButton bB = findViewById(R.id.radioButtonB);
    private RadioButton bC = findViewById(R.id.radioButtonC);
    private RadioButton bD = findViewById(R.id.radioButtonD);
    */

    int questionNumber;
    ArrayList<String> userAnswers;
    ArrayList<Integer> questionsIndex;

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
            }
        });

        setFields();
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        Intent i = getIntent();
        questionNumber = i.getExtras().getInt("questionNumber");

        // Get random questions index from last activity
        questionsIndex = i.getIntegerArrayListExtra("questionsIndex");

        // Get user's answers from last activity
        userAnswers = i.getStringArrayListExtra("userAnswers");
    }

    public void setFields() {
        questionText.setText("Texto de pregunta");
        radioButtonA.setText("A. Respuesta A");
        radioButtonB.setText("B. Respuesta B");
        radioButtonC.setText("C. Respuesta C");
        radioButtonD.setText("D. Respuesta D");
    }

    @OnClick(R.id.nextButton)
    public void submit() {
        // Toast.makeText(this, "You win!", LENGTH_SHORT).show();

        int selectedId = radioGroup.getCheckedRadioButtonId();

        String buttonSelected;

        // find which radioButton is checked by id
        if (selectedId == R.id.radioButtonA) {
            buttonSelected = "A";
        } else if (selectedId == R.id.radioButtonB) {
            buttonSelected = "B";
        } else if (selectedId == R.id.radioButtonC) {
            buttonSelected = "C";
        } else if (selectedId == R.id.radioButtonD) {
            buttonSelected = "D";
        }else {
            buttonSelected = "N/A";
        }

        Toast.makeText(getApplicationContext(), "Answer :  " + buttonSelected, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(QuestionActivity.this, QuestionActivity.class);

        intent.putExtra("questionNumber", questionNumber + 1);
        intent.putStringArrayListExtra("array", userAnswers);
        intent.putIntegerArrayListExtra("questionsIndex", questionsIndex);

        startActivity(intent);
    }

}
