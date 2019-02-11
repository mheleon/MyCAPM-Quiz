package com.mheleon.mycapmquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mheleon.mycapmquiz.controllers.CheckAnswerAdapter;
import com.mheleon.mycapmquiz.models.CapmQA;
import com.mheleon.mycapmquiz.models.CheckAnswer;
import com.mheleon.mycapmquiz.models.User;
import com.mheleon.mycapmquiz.models.UserScore;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class ResultsActivity extends AppCompatActivity {

    @BindView(R.id.score) TextView score;
    @BindView(R.id.tryAgainButton) Button tryAgainButton;

    int questionNumber; // Corresponde al numero de la pregunta, no al index del arreglo
    ArrayList<String> userAnswers = new ArrayList<>();
    ArrayList<Integer> questionsIndex;
    ArrayList<Boolean> results = new ArrayList<>();
    int res = 0;
    ArrayList<CheckAnswer> answerList = new ArrayList<>();
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        Intent i = getIntent();
        questionNumber = i.getExtras().getInt("questionNumber");

        // Get random questions index from last activity
        questionsIndex = i.getIntegerArrayListExtra("questionsIndex");

        // Get user's answers from last activity
        userAnswers = i.getStringArrayListExtra("userAnswers");

        // Getting and calculating score
        getScore();

        FloatingActionButton fab = findViewById(R.id.fabBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareScore();
                tryAgainButton();
            }
        });

        mRecyclerView = findViewById(R.id.my_recycler_view);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        CheckAnswerAdapter ca = new CheckAnswerAdapter(answerList);
        mRecyclerView.setAdapter(ca);
    }

    public void getScore() {

        Realm realm = Realm.getDefaultInstance();
        CapmQA capmQA;

        for (int i = 0; i <= 9; i++) {
            capmQA = realm.where(CapmQA.class).equalTo("id", questionsIndex.get(i)).findFirst();
            if (userAnswers.get(i).equals(capmQA.getAnswer())) {
                results.add(true);
                res++;
            } else {
                results.add(false);
            }
            answerList.add(new CheckAnswer("Question " + Integer.toString(i+1) + ".", capmQA.getQuestion(), optionToAnswerText(capmQA, userAnswers.get(i)), optionToAnswerText(capmQA, capmQA.getAnswer())));

        }
        realm.close();

        score.setText("Your score: " + Integer.toString(res) + "/10");
    }

    public String optionToAnswerText(CapmQA capmQA, String option) {
        switch (option) {
            case "a":
                return "A. " + capmQA.getA();
            case "b":
                return "B. " + capmQA.getB();
            case "c":
                return "C. " + capmQA.getC();
            default:
                return "D. " + capmQA.getD();
        }
    }

    @OnClick(R.id.tryAgainButton)
    public void tryAgainButton() {
        Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void shareScore () {
        // Get nickname from local DB
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        Log.d("nicknameDB", user.getNickname());

        // Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mDataBase = database.getReference("scoreTraining");

        mDataBase.child(user.getNickname()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserScore userScore = dataSnapshot.getValue(UserScore.class);
                    mDataBase.child(userScore.getUser()).child("score").setValue(userScore.getScore() + res);
                    mDataBase.child(userScore.getUser()).child("shared").setValue(userScore.getShared() + 1);

                } else {
                    final UserScore userScore = new UserScore("Anonymous", "France", "Rennes", res, 1, 1);
                    mDataBase.child(userScore.getUser()).setValue(userScore);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FireBase", "FireBase error: " + databaseError);
            }
        });
        Toast.makeText(this, "Score shared!", Toast.LENGTH_SHORT).show();
    }
}
