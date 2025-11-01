package com.tuke.bakalarka;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner_station;
    private Button scan_button;
    private CheckBox checkBox_registration;
    private int selectedStation;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        spinner_station = findViewById(R.id.station_id);
        scan_button = findViewById(R.id.button_scan);
        checkBox_registration = findViewById(R.id.checkBox_registration);

        setSpinnerStation();
        scan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                intent.putExtra("registrationChecked", checkBox_registration.isChecked());
                startActivity(intent);
            }
        });
    }

    private void setSpinnerStation() {
        Integer[] station = {1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, station);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_station.setAdapter(adapter);

        spinner_station.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStation = station[position];
                StationId id_station = (StationId) getApplication();
                id_station.setId(selectedStation);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
