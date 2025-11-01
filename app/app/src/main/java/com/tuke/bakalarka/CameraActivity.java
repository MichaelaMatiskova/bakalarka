package com.tuke.bakalarka;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tuke.bakalarka.api.QrCodeApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CameraActivity extends AppCompatActivity {

    private String scannedId;
    private boolean registrationChecked;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);

        registrationChecked = getIntent().getBooleanExtra("registrationChecked", false);
        scanQrCode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scanQrCode();
    }

    private void scanQrCode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a barcode or QR Code");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setCaptureActivity(PortraitScanActivity.class);
        intentIntegrator.initiateScan();
    }

    private void setIntent() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.10.19:80/api/qrcode/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        QrCodeApi api = retrofit.create(QrCodeApi.class);
        Call<Boolean> call = api.isUsed(scannedId);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                Intent intent = null;
                if (response.isSuccessful() && Boolean.TRUE.equals(response.body()) && !registrationChecked) {
                    intent = new Intent(getApplicationContext(), ScoringActivity.class);
                    intent.putExtra("id", scannedId);
                }
                else if (response.isSuccessful() && Boolean.TRUE.equals(response.body()) && registrationChecked) {
                    Toast.makeText(CameraActivity.this, "QR kód už je zaregistrovaný", Toast.LENGTH_SHORT).show();
                    scanQrCode();
                }
                else {
                    if (registrationChecked) {
                        intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                        intent.putExtra("id", scannedId);
                    } else {
                        Toast.makeText(CameraActivity.this, "QR kód nie je zaregistrovaný", Toast.LENGTH_SHORT).show();
                        scanQrCode();
                    }
                }
                if (intent != null)
                    startActivity(intent);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(CameraActivity.this, "Chyba Citania: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                scannedId = intentResult.getContents();
                Toast.makeText(this, "QR ID: " + scannedId, Toast.LENGTH_SHORT).show();
                setIntent();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
