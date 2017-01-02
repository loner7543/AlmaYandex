package com.almayandex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.almayandex.adapters.PointsAdapter;

import java.util.LinkedList;
import java.util.List;

import ru.yandex.yandexmapkit.utils.GeoPoint;
import ru.yandex.yandexmapkit.utils.Point;

public class FreePointsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,View.OnClickListener {
    private Button deletePointButton;
    private List<GeoPoint> data;
    private ListView listView;
    private Intent intent;
    private int dataCount;
    private PointsAdapter pointsAdapter;
    private GeoPoint selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_points);
        listView = (ListView) findViewById(R.id.free_points_list);
        deletePointButton = (Button) findViewById(R.id.removeBtn);
        deletePointButton.setOnClickListener(this);
        data = new LinkedList<>();
        intent = getIntent();
        dataCount = intent.getIntExtra("count",100);
        for (int j = 0;j<dataCount;j++){
            GeoPoint geoPoint = new GeoPoint(intent.getDoubleExtra("lat"+j,0.0),intent.getDoubleExtra("lon"+j,0.0));
            data.add(geoPoint);
        }
        pointsAdapter = new PointsAdapter(this,R.layout.point_item,data);
        listView.setAdapter(pointsAdapter);
        listView.setOnItemClickListener(this);

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectedItem = (GeoPoint) adapterView.getItemAtPosition(i);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.putExtra("lat",selectedItem.getLat());
        intent.putExtra("lon",selectedItem.getLon());
        setResult(RESULT_OK,intent);
        finish();
    }
}
