package com.almayandex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.almayandex.adapters.TravelAdapter;

import java.util.LinkedList;
import java.util.List;

import ru.yandex.yandexmapkit.utils.GeoPoint;

public class TravelActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,View.OnClickListener {
    private Intent intent;
    private Travel selectegTravel;
    private GeoPoint fromPoint;
    private GeoPoint toPoint;
    private List<Travel> data;
    private int count;
    private int color;
    private String name;
    private ListView travels;
    private Button deleteBtn;
    private TravelAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        travels = (ListView) findViewById(R.id.travels_list);
        data = new LinkedList<>();
        intent = getIntent();
        count = intent.getIntExtra("count",100);
        for (int i = 0;i<count;i++){
            name = intent.getStringExtra("name"+i);
            color = intent.getIntExtra("color"+i,0);
            fromPoint = new GeoPoint(intent.getDoubleExtra("fromLat"+i,0.0),intent.getDoubleExtra("fromLon"+i,0.0));//(lat,lon)
            toPoint = new GeoPoint(intent.getDoubleExtra("toLat"+i,0.0),intent.getDoubleExtra("toLon"+i,0.0));//(lat,lon)
            data.add(new Travel(fromPoint,toPoint,name,color));
        }
        adapter = new TravelAdapter(this,R.layout.travel_item,data);
        travels.setAdapter(adapter);
        travels.setOnItemClickListener(this);
        deleteBtn = (Button) findViewById(R.id.deleteTravelBtn);
        deleteBtn.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectegTravel = (Travel) adapterView.getItemAtPosition(i);
    }

    @Override
    public void onClick(View view) {
        intent = new Intent();
        intent.putExtra("fromPointLat",selectegTravel.getStartPoint().getLat());
        setResult(RESULT_OK,intent);
        finish();
    }
}
