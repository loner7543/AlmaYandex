package com.almayandex;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import ru.yandex.yandexmapkit.utils.GeoPoint;

public class AddTravelActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner colorSpinner;
    private Spinner fromPointSpinner;
    private Spinner toPointSpinner;
    private  int color;
    private Button addTravelBtn;
    private GeoPoint fromPoint;
    private GeoPoint toPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addTravelBtn = (Button) findViewById(R.id.onSend);
        addTravelBtn.setOnClickListener(this);
        fromPointSpinner = (Spinner) findViewById(R.id.startPointSpinner);
        fromPointSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fromPoint = (GeoPoint) adapterView.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        toPointSpinner = (Spinner) findViewById(R.id.endPointSpinner);
        colorSpinner = (Spinner) findViewById(R.id.color_spinner);
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = (String) adapterView.getSelectedItem();
                switch (selected){
                    case "Красный":{
                        color = Color.RED;
                        break;
                    }
                    case "Синий":{
                        color = Color.BLUE;
                        break;
                    }
                    case "Зеленый":{
                        color = Color.GREEN;
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        String d = "";
    }
}
