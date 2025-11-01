package com.tuke.bakalarka;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.tuke.bakalarka.api.CompetitorApi;
import com.tuke.bakalarka.api.ScoreApi;
import com.tuke.bakalarka.model.Competitor;
import com.tuke.bakalarka.model.Score;
import com.tuke.bakalarka.model.Station;

import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScoringActivity extends AppCompatActivity {
    private String scannedId;
    private TextView name;
    private TextView surname;
    private TextView age;
    private Spinner station_spinner;
    private Spinner points_spinner;

    private int selectedPoint;
    private int selectedStation;
    private Competitor c;
    private Button submit_btn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoring);

        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        age = findViewById(R.id.age);
        station_spinner = findViewById(R.id.station);
        points_spinner = findViewById(R.id.points);
        submit_btn = findViewById(R.id.submit_button);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            scannedId = extras.getString("id");
        }

        StationId id_station = (StationId) getApplication();
        selectedStation = id_station.getId();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.10.19:80/api/competitors/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CompetitorApi api = retrofit.create(CompetitorApi.class);
        Call<Competitor> call = api.findById(scannedId);

        call.enqueue(new Callback<Competitor>() {
            @Override
            public void onResponse(Call<Competitor> call, Response<Competitor> response) {
                if (response.isSuccessful() && response.body() != null) {
                    c = response.body();
                    name.setText(c.getName());
                    surname.setText(c.getSurname());
                    age.setText(String.valueOf(c.getAge()));
                } else {
                    Toast.makeText(ScoringActivity.this, "Nepodarilo sa načítať súťažiaceho", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Competitor> call, Throwable t) {

                Toast.makeText(ScoringActivity.this, "Pruser bodov" + t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });

        setSpinnerPoints();
        setSpinnerStation();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.10.19:80/api/score/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ScoreApi scoreApi = retrofit.create(ScoreApi.class);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (c != null) {
                    Score score = new Score(new Station((long) selectedStation), new Competitor(Long.parseLong(scannedId)), selectedPoint);
                    Call<Score> callScore = scoreApi.addScore(score);

                    callScore.enqueue(new Callback<Score>() {

                        @Override
                        public void onResponse(Call<Score> call, Response<Score> response) {
                            System.out.println("HTTP code: " + response.code());
                            System.out.println("Body: " + response.body());

                            if (response.isSuccessful() && response.body() != null)
                                Toast.makeText(ScoringActivity.this, "uspesne zapisane body", Toast.LENGTH_SHORT).show();
                            else {
                                if (response.code() == 409)
                                    Toast.makeText(ScoringActivity.this, "Body pre toto stanoviste su uz zapisane", Toast.LENGTH_SHORT).show();
                                else {
                                    Toast.makeText(ScoringActivity.this, "Nezapisane body", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Score> call, Throwable t) {
                            Toast.makeText(ScoringActivity.this, "Pruser bodov pri zapise" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(ScoringActivity.this, "Súťažiaci sa nenačítal", Toast.LENGTH_SHORT).show();
                }
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(intent);
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);

    }

    private void setSpinnerPoints() {
        Integer[] points = {1,2,3,4,5};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, points);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        points_spinner.setAdapter(adapter);

        points_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPoint = points[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setSpinnerStation() {
        Integer[] stations = {1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        station_spinner.setAdapter(adapter);

        station_spinner.setSelection(selectedStation - 1);

        station_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStation = stations[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}