package com.mheleon.mycapmquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AllQuestionsActivity extends AppCompatActivity {
    ListView listView;
    String[] q = new String[] {"Question 1", "Question 2", "Question 3", "Question 4", "Question 5", "Question 6", "Question 7", "Question 8", "Question 9", "Question 10"};

    // ListView list;
    ArrayList<String> titles = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    List<CapmQA> questionsList = new ArrayList<>();
    List<String> answers = new ArrayList<>(10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_questions);
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

        /* Gestion de lista */
        listView = findViewById(R.id.questionList);
        // El adaptador indica como se van a mostrar las cosas
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, q);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Position " + position, Toast.LENGTH_SHORT).show();
            }
        });


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titles);
        // list = findViewById(R.id.list);
        // list.setAdapter(arrayAdapter);

        getQuestionAnswers();



    }

    /**
     * Method to get questions and answers from api
     */
    private void getQuestionAnswers() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://bridge.buddyweb.fr")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CapmQAService capmQAService = retrofit.create(CapmQAService.class);
        Call<List<CapmQA>> call = capmQAService.getCapmQA();



        call.enqueue(new Callback<List<CapmQA>>() {
            @Override
            public void onResponse(Call<List<CapmQA>> call, Response<List<CapmQA>> response) {
                for(int i = 0; i < 10; i++) {
                    questionsList.add(response.body().get((int) ((Math.random() * response.body().size()) - 1)));

                }
                // Log.d("size", Integer.toString(questionsList.size()));

                for(CapmQA campQA : response.body()) {
                    Log.d("quest", campQA.getQuestion());
                }
            }

            Intent intent = new Intent(AllQuestionsActivity.this, QuestionActivity.class);

            @Override
            public void onFailure(Call<List<CapmQA>> call, Throwable t) {
            }
        });

    }

}
