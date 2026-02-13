package com.tuke.bakalarka;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.tuke.bakalarka.api.CompetitorApi;
import com.tuke.bakalarka.model.Competitor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RegistrationActivity extends AppCompatActivity {

    private String scannedId;
    private EditText name;
    private EditText surname;
    private EditText age;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            scannedId = extras.getString("id");
        }

        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        age = findViewById(R.id.age);

        Button registerButt = findViewById(R.id.registerButton);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);

        registerButt.setOnClickListener(view -> {
            if (scannedId != null) {
                sendCompetitorToBackend((long) Integer.parseInt(scannedId));
                finish();
            } else {
                Toast.makeText(this, "Naskenuj QR KOD!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendCompetitorToBackend(Long id) {
        String nameText = name.getText().toString().trim();
        String surnameText = surname.getText().toString().trim();
        String ageText = age.getText().toString().trim();

        if (nameText.isEmpty()) {
            name.setError("Meno je povinné");
            name.requestFocus();
            return;
        } if (surnameText.isEmpty()) {
            surname.setError("Priezvisko je povinné");
            surname.requestFocus();
            return;
        } if (ageText.isEmpty()) {
            age.setError("Vek je povinný");
            age.requestFocus();
            return;
        }

        Competitor competitor = new Competitor(id, name.getText().toString(), surname.getText().toString(), Integer.parseInt(age.getText().toString()));

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.10.226:80/api/competitors/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CompetitorApi api = retrofit.create(CompetitorApi.class);
        Call<Competitor> call = api.register(competitor);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Competitor> call, @NonNull Response<Competitor> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegistrationActivity.this, "Uspesna registracia", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegistrationActivity.this, "QR kod je uz (pravdepodobne) pouzity", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Competitor> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, "Chyba Registracie: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }
}