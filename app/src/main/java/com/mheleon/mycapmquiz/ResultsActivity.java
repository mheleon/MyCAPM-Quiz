package com.mheleon.mycapmquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class ResultsActivity extends AppCompatActivity {

    @BindView(R.id.score)
    TextView score;

    int questionNumber; // Corresponde al numero de la pregunta, no al index del arreglo
    ArrayList<String> userAnswers = new ArrayList<>();
    ArrayList<Integer> questionsIndex;
    ArrayList<Boolean> resultats = new ArrayList<>();
    int res = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
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

        Intent i = getIntent();
        questionNumber = i.getExtras().getInt("questionNumber");

        // Get random questions index from last activity
        questionsIndex = i.getIntegerArrayListExtra("questionsIndex");

        // Get user's answers from last activity
        userAnswers = i.getStringArrayListExtra("userAnswers");
        // Log.d("size user -- answer", Integer.toString(userAnswers.size()));
        Log.d("currentQuest", Integer.toString(questionNumber));

        getScore();
        setTitle("Your score: " + Integer.toString(res)+ "/10");
    }

    public void getScore() {

        Realm realm = Realm.getDefaultInstance();
        CapmQA capmQA;

        for (int i = 0; i <= 9; i++) {
            capmQA = realm.where(CapmQA.class).equalTo("id", questionsIndex.get(i)).findFirst();
            Log.d("answer=result", userAnswers.get(i) + "==" + capmQA.getAnswer());
            if (userAnswers.get(i).equals(capmQA.getAnswer())) {
                resultats.add(true);
                res++;
            } else {
                resultats.add(false);
            }

        }
        realm.close();
    }


}
