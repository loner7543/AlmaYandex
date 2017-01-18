package com.almayandex;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.almayandex.adapters.PointsAdapter;
import com.almayandex.domain.MyPoint;

import java.util.LinkedList;
import java.util.List;

import ru.yandex.yandexmapkit.utils.GeoPoint;

public class FreePointsScrollingActivity extends AppCompatActivity  implements AdapterView.OnItemClickListener,View.OnClickListener{
    public static final int CAMERA_RESULT_ADD = 0;
    private Button deletePointButton;
    private List<MyPoint> data;
    private ListView listView;
    private Intent intent;
    private int dataCount;
    private PointsAdapter pointsAdapter;
    private MyPoint selectedItem;
    private DbUtils utils;
    private SQLiteDatabase sqLiteDatabase;
    private List<MyPoint> dbBData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_points_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        utils = new DbUtils(this, DbUtils.DATABASE_NAME, DbUtils.DATABASE_VERSION);
        sqLiteDatabase = utils.getWritableDatabase();//дает бд на запись
        dbBData = utils.getAllPoints(sqLiteDatabase);
        pointsAdapter = new PointsAdapter(this,R.layout.point_item,data,dbBData);
        listView.setAdapter(pointsAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedItem = (MyPoint) parent.getItemAtPosition(position);
    }

    @Override
    public void onClick(View v) {
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

    public void onAddDescription(View view){
        LayoutInflater layoutInflater = LayoutInflater.from(FreePointsScrollingActivity.this);
        View promptView = layoutInflater.inflate(R.layout.edit_dialog, null);
        final EditText editText = (EditText) promptView.findViewById(R.id.point_desc);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FreePointsScrollingActivity.this);
        alertDialogBuilder.setTitle("Введите описание для точки");
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(false)
                .setNegativeButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String text = editText.getText().toString();
                                MyPoint goal = null;
                                for (MyPoint myPoint:pointsAdapter.getData())
                                {
                                    if (myPoint.getGeoPoint()==selectedItem.getGeoPoint()){
                                        goal = myPoint;
                                    }
                                }
                                goal.setDescription(text);
                                selectedItem.setDescription(text);
                                pointsAdapter.notifyDataSetChanged();
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
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
                    dataItem.setPhotos(thumbnailBitmap);
                    utils.insertPoint(dataItem,sqLiteDatabase);

                    pointsAdapter.getDbPoints().add(dataItem);
                    pointsAdapter.notifyDataSetChanged();
                }
                catch (Exception e){
                    Toast toast = Toast.makeText(this,"Действие было отменено пользователем",Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
            }
        }
    }
}
