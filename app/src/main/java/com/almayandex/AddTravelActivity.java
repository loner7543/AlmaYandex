package com.almayandex;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.almayandex.adapters.AddTravelAdapter;
import com.almayandex.adapters.PointsAdapter;
import com.almayandex.domain.MyPoint;

import java.util.LinkedList;
import java.util.List;

import ru.yandex.yandexmapkit.utils.GeoPoint;

public class AddTravelActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner colorSpinner;
    private Spinner fromPointSpinner;
    private Spinner toPointSpinner;
    private  int color;
    private Button addTravelBtn;
    private List<MyPoint> travelsPoints;//список свободных точек из которых будем делать путешевствия
    private MyPoint fromPoint;
    private MyPoint toPoint;
    private Intent intent;
    private AddTravelAdapter pointsAdapter;
    private int count;
    private EditText travelNameEdit;
    private DbUtils utils;
    private SQLiteDatabase sqLiteDatabase;
    private List<MyPoint> dbPoints;// точки в бд с фотками

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        travelNameEdit = (EditText) findViewById(R.id.add_travel_name);
        intent = getIntent();
        count = intent.getIntExtra("count",100);
        utils = new DbUtils(this, DbUtils.DATABASE_NAME, DbUtils.DATABASE_VERSION);
        sqLiteDatabase = utils.getWritableDatabase();//дает бд на запись
        dbPoints = utils.getAllPoints(sqLiteDatabase);
        travelsPoints = new LinkedList<>();
        for (int i = 0;i<count;i++){
            travelsPoints.add(new MyPoint(new GeoPoint(intent.getDoubleExtra("lat"+i,0.0),intent.getDoubleExtra("lon"+i,0.0))));
        }
        pointsAdapter = new AddTravelAdapter(this,R.layout.point_item,travelsPoints,dbPoints);
        addTravelBtn = (Button) findViewById(R.id.onSend);
        addTravelBtn.setOnClickListener(this);
        fromPointSpinner = (Spinner) findViewById(R.id.startPointSpinner);
        fromPointSpinner.setAdapter(pointsAdapter);
        fromPointSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fromPoint = (MyPoint) adapterView.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        toPointSpinner = (Spinner) findViewById(R.id.endPointSpinner);
        toPointSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toPoint = (MyPoint) adapterView.getSelectedItem();
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
        intent.putExtra("fromPointLat",fromPoint.getGeoPoint().getLat());
        intent.putExtra("fromPointLon",fromPoint.getGeoPoint().getLon());

        intent.putExtra("toPointLat",toPoint.getGeoPoint().getLat());
        intent.putExtra("toPointLon",toPoint.getGeoPoint().getLon());

        intent.putExtra("color",color);
        intent.putExtra("travel_name",travelNameEdit.getText().toString());
        setResult(RESULT_OK,intent);
        finish();
    }
}
