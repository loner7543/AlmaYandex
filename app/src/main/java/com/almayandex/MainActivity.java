package com.almayandex;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.almayandex.adapters.ImageAdapter;
import com.almayandex.geo.OverlayGeoCode;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.map.MapEvent;
import ru.yandex.yandexmapkit.map.OnMapListener;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.location.MyLocationItem;
import ru.yandex.yandexmapkit.overlay.location.OnMyLocationListener;
import ru.yandex.yandexmapkit.utils.GeoPoint;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,OnMapListener,OnMyLocationListener,AdapterView.OnItemSelectedListener {
    public static final String TAG = "MainActivity";
    public static final int SEARCH_ADDRESS_CODE = 1;
    public static final int WAYS_CODE=2;
    private MapController mMapController;
    private LocationManager locationManager;
    private MapView mapView;
    private Spinner imageSpinner;
    private Context context;
    private boolean isGPSProviderEnabled;//включен ли GPS/ нет покажем тоаст
    private Overlay currentOverlay;
    private OverlayManager overlayManager;
    private OverlayItem overlayItem;
    private GeoPoint geoPoint;
    private Geocoder geocoder;
    private Address address;
    private Intent intent;
    private List<Bitmap> markers;
    private ImageAdapter spinnerAdapter;
    private Bitmap selectedMarker;
    private BitmapDrawable res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mapView = (MapView) findViewById(R.id.map);
        imageSpinner = (Spinner) findViewById(R.id.image_spinner);
        mapView.showBuiltInScreenButtons(true);
        mMapController = mapView.getMapController();
        geocoder = new Geocoder(this, Locale.getDefault());//TODO Возможны проблемы с локалью

        context = getApplicationContext();
        mMapController.addMapListener(this);
        mMapController.getOverlayManager().getMyLocation().addMyLocationListener(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //checkEnabled();
        overlayManager = mMapController.getOverlayManager();
        currentOverlay = new Overlay(mMapController);
        initMarkers();
        spinnerAdapter = new ImageAdapter(context,R.layout.spinner_item,markers);
        imageSpinner.setAdapter(spinnerAdapter);
        imageSpinner.setOnItemSelectedListener(this);
        imageSpinner.setSelection(1);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:{
                Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                break;
            }
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.ways:{
                break;
            }
            case R.id.adr_search:{
                intent = new Intent(this,AdrActivity.class);
                startActivityForResult(intent,SEARCH_ADDRESS_CODE);
                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean checkEnabled() {
        isGPSProviderEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isGPSProviderEnabled;
    }

    @Override
    public void onMapActionEvent(MapEvent mapEvent) {

    }

    @Override
    public void onMyLocationChange(MyLocationItem myLocationItem) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case SEARCH_ADDRESS_CODE:{
                    String addr = data.getStringExtra("addr");
                    try {
                        List<Address> addresses = geocoder.getFromLocationName(addr,1);
                        Address adr = addresses.get(0);
                        GeoPoint geoPoint = new GeoPoint(adr.getLatitude(),adr.getLongitude());
                        overlayItem = new OverlayItem(geoPoint,context.getResources().getDrawable(R.drawable.a));//TODO пока захардкодил иконку
                        currentOverlay.addOverlayItem(overlayItem);//
                        mMapController.getOverlayManager().addOverlay(currentOverlay);//TODO не уверен что нужно добавлять  новый слой
                    } catch (IOException e) {
                        Log.d(TAG,"Address callback crashed");
                        Toast toast = Toast.makeText(this,"адрес указан неверно",Toast.LENGTH_LONG);
                        toast.show();
                        e.printStackTrace();
                    }
                    break;
                }
                case WAYS_CODE:{
                    break;
                }
                default:{
                    break;
                }
            }
        }
    }

    public void initMarkers(){
        markers = new LinkedList<>();
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.a);
        markers.add(icon);
        icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.shop);
        markers.add(icon);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedMarker = (Bitmap) adapterView.getSelectedItem();
        res = new BitmapDrawable(context.getResources(),selectedMarker);
        overlayManager.addOverlay(new OverlayGeoCode(mapView.getMapController(),getApplicationContext(),currentOverlay,res));//подписал контроллер на событие тапа по карте в произв месте
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
