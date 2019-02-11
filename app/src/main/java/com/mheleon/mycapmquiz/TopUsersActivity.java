package com.mheleon.mycapmquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mheleon.mycapmquiz.models.UserScore;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopUsersActivity extends AppCompatActivity {

    @BindView(R.id.user1) TextView user1;
    @BindView(R.id.user2) TextView user2;
    @BindView(R.id.user3) TextView user3;
    @BindView(R.id.score1) TextView score1;
    @BindView(R.id.score2) TextView score2;
    @BindView(R.id.score3) TextView score3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_users);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TopUsersActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ButterKnife.bind(this);

        getTopUsers();
    }

    public void getTopUsers () {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mDataBase = database.getReference("scoreTraining");

        Query query = mDataBase.orderByChild("score").limitToLast(3);

        // Top users by score
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<UserScore> topUsers = new ArrayList<>();
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    UserScore userScore = userSnapshot.getValue(UserScore.class);
                    topUsers.add(userScore);
                }
                user1.setText(topUsers.get(2).getUser());
                score1.setText(Integer.toString(topUsers.get(2).getScore()));
                user2.setText(topUsers.get(1).getUser());
                score2.setText(Integer.toString(topUsers.get(1).getScore()));
                user3.setText(topUsers.get(0).getUser());
                score3.setText(Integer.toString(topUsers.get(0).getScore()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("error", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

}
