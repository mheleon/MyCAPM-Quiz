package com.mheleon.mycapmquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    int nbQuestions = 10;

    @BindView(R.id.startButton) Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.startButton)
    public void startQuiz() {
        Toast.makeText(this, "Starting...", Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(MainActivity.this, AllQuestionsActivity.class);
        Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
        intent.putExtra("questionNumber", 1);

        ArrayList<String> userAnswers = new ArrayList<>(nbQuestions);
        intent.putStringArrayListExtra("array", userAnswers);

        intent.putIntegerArrayListExtra("questionsIndex", getRandomQuestions(nbQuestions));
        startActivity(intent);
    }

    /**
     * Method to get list of index random questions
     *
     * @param nbQuestions
     * @return Index list of questions
     */
    private ArrayList<Integer> getRandomQuestions(int nbQuestions) {
        ArrayList<Integer> questionsIndex = new ArrayList<>(nbQuestions);
        for(int i = 0; i < nbQuestions; i++) {
            // questionsIndex.add(response.body().get((int) ((Math.random() * response.body().size()) - 1)));
            questionsIndex.add((int) (Math.random() * 21) - 1);
        }
        return questionsIndex;
    }
}