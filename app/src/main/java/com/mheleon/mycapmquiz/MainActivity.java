package com.mheleon.mycapmquiz;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mheleon.mycapmquiz.controllers.CapmQAService;
import com.mheleon.mycapmquiz.models.CapmQA;
import com.mheleon.mycapmquiz.models.User;
import com.mheleon.mycapmquiz.models.UserScore;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    int nbQuestions = 10;
    RealmList<CapmQA> resource = new RealmList<>();

    @BindView(R.id.nickname) EditText nickname;
    @BindView(R.id.startButton) Button startButton;

    Dialog myDialog;

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
                // Snackbar.make(view, "Hola!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                ShowPopup(view);
            }
        });

        if (isNetworkAvailable()) {
            Realm.init(getApplicationContext());

            checkDB();

            getNicknameField();
        } else {
            Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
        }

        myDialog = new Dialog(this);
    }

    /**
     * Method to add items to the action bar
     *
     * @param menu Menu
     * @return True if success
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Handle action bar item
     *
     * @param item Menu item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            if (!isNetworkAvailable()) {
                Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                return false;
            }
            Intent intent = new Intent(MainActivity.this, TopUsersActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_update) {
            if (!isNetworkAvailable()) {
                Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                return false;
            }
            getApiToDB();
            Toast.makeText(getApplicationContext(), "Quiz updated!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_close) {
            Toast.makeText(getApplicationContext(), "Bye!", Toast.LENGTH_SHORT).show();
            finish();
            System.exit(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Method to manage start button
     */
    @OnClick(R.id.startButton)
    public void startQuiz() {

        if (!isNetworkAvailable()) {
            Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        if (nickname.getText().length() < 1) {
            Toast.makeText(this, "Please enter a nickname before continue", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!nickname.getText().toString().matches("[a-zA-Z0-9]+")) {
            Toast.makeText(this, "Please enter an alphanumeric nickname", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Starting training...", Toast.LENGTH_SHORT).show();

        createUser();

        // Save nickname in local DB
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(User.class);
        User user = realm.createObject(User.class);
        user.setNickname(nickname.getText().toString());
        realm.commitTransaction();

        Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
        intent.putExtra("questionNumber", 1);

        ArrayList<String> userAnswers = new ArrayList<>();
        for (int i = 0; i < nbQuestions; i++) userAnswers.add("x");
        intent.putStringArrayListExtra("userAnswers", userAnswers);

        intent.putIntegerArrayListExtra("questionsIndex", getRandomQuestions(nbQuestions));
        startActivity(intent);
    }

    /**
     * Method to get list of index random questions
     *
     * @param nbQuestions number of questions
     * @return Index list of questions
     */
    private ArrayList<Integer> getRandomQuestions(int nbQuestions) {
        ArrayList<Integer> questionsIndex = new ArrayList<>(nbQuestions);

        Realm realm = Realm.getDefaultInstance();
        int nbQuestionsDB = realm.where(CapmQA.class).findAll().size();
        for (int i = 0; i < nbQuestions; i++) {
            // questionsIndex.add(response.body().get((int) ((Math.random() * response.body().size()) - 1)));
            // nbQuestionsDB is the maximum and the 1 is the minimum.
            int rand = (int) (Math.random() * nbQuestionsDB + 1);
            questionsIndex.add(rand);
        }
        return questionsIndex;
    }

    /**
     * Method to get Questions from API and add them to DB
     */
    public void getApiToDB() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(CapmQA.class);
        realm.commitTransaction();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://bridge.buddyweb.fr")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CapmQAService capmQAService = retrofit.create(CapmQAService.class);
        Call<RealmList<CapmQA>> call = capmQAService.getCapmQA();

        call.enqueue(new Callback<RealmList<CapmQA>>() {
            @Override
            public void onResponse(Call<RealmList<CapmQA>> call, Response<RealmList<CapmQA>> response) {
                resource = response.body();
                Realm realm = Realm.getDefaultInstance();
                try {
                    // add response to realm database
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(resource);
                    realm.commitTransaction();
                    realm.close();
                } catch (Throwable e) {
                    if (realm.isInTransaction()) {
                        realm.cancelTransaction();
                    }
                    throw e;
                }
            }

            @Override
            public void onFailure(Call<RealmList<CapmQA>> call, Throwable t) {
                Log.d("fail", "response fail");
            }
        });
    }

    /**
     * Create anonymous user in Firebase
     */
    public void createUser() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mDataBase = database.getReference("scoreTraining");

        mDataBase.child(nickname.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserScore userScore = dataSnapshot.getValue(UserScore.class);
                    mDataBase.child(userScore.getUser()).child("counter").setValue(userScore.getCounter() + 1);
                } else {
                    final UserScore userScore = new UserScore(nickname.getText().toString(), "", "", 0, 1, 0);
                    mDataBase.child(userScore.getUser()).setValue(userScore);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FireBase", "FireBase error: " + databaseError);
            }
        });
    }

    /**
     * Method to get user from local DB if exists
     */
    public void getNicknameField() {
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        if (user != null) {
            nickname.setText(user.getNickname());
            nickname.setSelection(nickname.getText().length());
        }

    }

    /**
     * Method that checks if there are questions in local DB, if false get them from API
     */
    public void checkDB() {
        Realm realm = Realm.getDefaultInstance();
        int nbQuestionsDB = realm.where(CapmQA.class).findAll().size();
        if (nbQuestionsDB <= 0) {
            getApiToDB();
        }

    }

    /**
     * Detect whether there is an Internet connection available
     * @return true if available
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void ShowPopup(View v) {
        TextView txtclose;
        Button btnFollow;
        myDialog.setContentView(R.layout.popup_info);
        txtclose = myDialog.findViewById(R.id.txtclose);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}