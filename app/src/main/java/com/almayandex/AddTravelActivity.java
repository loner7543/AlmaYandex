package com.almayandex;

import android.content.Intent;
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

import com.almayandex.adapters.PointsAdapter;

import java.util.LinkedList;
import java.util.List;

import ru.yandex.yandexmapkit.utils.GeoPoint;

public class AddTravelActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner colorSpinner;
    private Spinner fromPointSpinner;
    private Spinner toPointSpinner;
    private  int color;
    private Button addTravelBtn;
    private List<GeoPoint> travelsPoints;//список свободных точек из которых будем делать путешевствия
    private GeoPoint fromPoint;
    private GeoPoint toPoint;
    private Intent intent;
    private PointsAdapter pointsAdapter;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intent = getIntent();
        count = intent.getIntExtra("count",100);
        travelsPoints = new LinkedList<>();
        for (int i = 0;i<count;i++){
            travelsPoints.add(new GeoPoint(intent.getDoubleExtra("lat"+i,0.0),intent.getDoubleExtra("lon"+i,0.0)));
        }
        pointsAdapter = new PointsAdapter(this,R.layout.point_item,travelsPoints);
        addTravelBtn = (Button) findViewById(R.id.onSend);
        addTravelBtn.setOnClickListener(this);
        fromPointSpinner = (Spinner) findViewById(R.id.startPointSpinner);
        fromPointSpinner.setAdapter(pointsAdapter);
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
        toPointSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toPoint = (GeoPoint) adapterView.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        toPointSpinner.setAdapter(pointsAdapter);
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
        intent = new Intent();
        intent.putExtra("fromPointLat",fromPoint.getLat());
        intent.putExtra("fromPointLon",fromPoint.getLon());

        intent.putExtra("toPointLat",toPoint.getLat());
        intent.putExtra("toPointLon",toPoint.getLon());

        intent.putExtra("color",color);
        setResult(RESULT_OK,intent);
        finish();
    }
}
