package com.almayandex;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
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
    public static final int CAMERA_RESULT_ADD = 0;
    private Button deletePointButton;
    private List<MyPoint> data;
    private ListView listView;
    private Intent intent;
    private int dataCount;
    private PointsAdapter pointsAdapter;
    private MyPoint selectedItem;

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
            data.add(new MyPoint(geoPoint));
        }
        pointsAdapter = new PointsAdapter(this,R.layout.point_item,data);
        listView.setAdapter(pointsAdapter);
        listView.setOnItemClickListener(this);

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectedItem = (MyPoint) adapterView.getItemAtPosition(i);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.putExtra("lat",selectedItem.getGeoPoint().getLat());
        intent.putExtra("lon",selectedItem.getGeoPoint().getLon());
        setResult(RESULT_OK,intent);
        finish();
    }

    public void  onAddPhoto(View view){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_RESULT_ADD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA_RESULT_ADD:{
                try{
                    Bitmap thumbnailBitmap = (Bitmap) data.getExtras().get("data");
                    MyPoint dataItem = null;//тот эдт в который добавим фото
                    for (MyPoint myPoint:pointsAdapter.getData()){
                        if (myPoint.getGeoPoint()==selectedItem.getGeoPoint()){
                            dataItem = myPoint;
                        }
                    }
                    dataItem.getPhotos().add(thumbnailBitmap);
                    pointsAdapter.notifyDataSetChanged();
                }
                catch (Exception e){

                }
                break;
        }
        }
    }
}
