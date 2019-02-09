package com.mheleon.mycapmquiz;

import android.content.Intent;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mheleon.mycapmquiz.models.UserScore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                Snackbar.make(view, "Hola!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Realm.init(getApplicationContext());

        // TODO check if db is updated
        getApiToDB();

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

    /**
     * Method to manage start button
     */
    @OnClick(R.id.startButton)
    public void startQuiz() {
        Toast.makeText(this, "Starting training...", Toast.LENGTH_SHORT).show();

        createUser();
        // Intent intent = new Intent(MainActivity.this, AllQuestionsActivity.class);
        Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
        intent.putExtra("questionNumber", 1);

        ArrayList<String> userAnswers = new ArrayList<>();
        for(int i = 0; i < nbQuestions; i ++ ) userAnswers.add("x");
        intent.putStringArrayListExtra("userAnswers", userAnswers);

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

        Realm realm = Realm.getDefaultInstance();
        int nbQuestionsDB = realm.where(CapmQA.class).findAll().size();
        Log.d("size", Integer.toString(nbQuestionsDB));
        for(int i = 0; i < nbQuestions; i++) {
            // questionsIndex.add(response.body().get((int) ((Math.random() * response.body().size()) - 1)));
            // nbQuestionsDB is the maximum and the 1 is the minimum.
            int rand = (int) (Math.random() * nbQuestionsDB + 1);
            Log.d("rand", Integer.toString(rand));
            questionsIndex.add(rand);
        }
        return questionsIndex;
    }

    private void testDB() {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(CapmQA.class);
        realm.commitTransaction();

        // realm.deleteAll();

// All writes are wrapped in a transaction
// to facilitate safe multi threading

        realm.beginTransaction();
        try {

            CapmQA capmQA = realm.createObject(CapmQA.class, 1);
            capmQA.setId_chapter(1);
            capmQA.setQuestion("primera pregunta");
            capmQA.setA("preguta a");
            capmQA.setB("preguta a");
            capmQA.setC("preguta a");
            capmQA.setD("preguta a");
            capmQA.setAnswer("b");

            // final CapmQA managedDog = realm.copyToRealm(capmQA);
            // Add a question
            realm.commitTransaction();
        } catch (Throwable e) {
            if (realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            throw e;
        }

        CapmQA capmQA2 = realm.where(CapmQA.class).equalTo("id", 1).findFirst();
        Log.d("dataB", capmQA2.getQuestion());

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

                int notesCount = realm.where(CapmQA.class).findAll().size();
                int res = realm.where(CapmQA.class).findAll().size();
                int res1 = realm.where(CapmQA.class).findAll().size();
                int res2 = realm.where(CapmQA.class).findAll().size();
                CapmQA capmQA2 = realm.where(CapmQA.class).equalTo("id", 1).findFirst();
                CapmQA capmQA3 = realm.where(CapmQA.class).equalTo("id", 5).findFirst();

                Log.d("dataDB", capmQA3.getQuestion());
                Log.d("dataDB", capmQA3.getA());
                Log.d("dataDB", capmQA3.getB());
                Log.d("dataDB", capmQA3.getC());
                Log.d("dataDB", capmQA3.getD());
                Log.d("dataDB", capmQA3.getAnswer());
            }

            @Override
            public void onFailure(Call<RealmList<CapmQA>> call, Throwable t) {
                Log.d("fail", "response fail");
            }
        });
    }

    public void createUser () {
        // Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mDataBase = database.getReference("scoreTraining");

        mDataBase.child("Anonymous").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("fireDB", "Existing user");
                    UserScore userScore = dataSnapshot.getValue(UserScore.class);
                    mDataBase.child(userScore.getUser()).child("counter").setValue(userScore.getCounter() + 1);
                } else {
                    Log.d("fireDB", "New user");
                    mDataBase.child("Anonymous").child("user").setValue("Anonymous");
                    mDataBase.child("Anonymous").child("counter").setValue(1);
                    mDataBase.child("Anonymous").child("shared").setValue(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FireBase", "FireBase error: " + databaseError);
            }
        });
    }
}