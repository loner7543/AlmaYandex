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
import android.location.Location;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnMapListener,OnMyLocationListener,AdapterView.OnItemSelectedListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final String TAG = "MainActivity";
    public static final int SEARCH_ADDRESS_CODE = 1;
    public static final int ADD_TRAVEL_CODE =2;
    public static final int FREE_POINTS_CODE = 3;
    public static final int REMOVE_TRAVEL_CODE  =4;
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
    private GoogleApiClient googleApiClient;
    private List<GeoPoint> freePoints;//вне путешествия

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

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

        overlayManager = mMapController.getOverlayManager();
        currentOverlay = new Overlay(mMapController);
        initMarkers();
        spinnerAdapter = new ImageAdapter(context,R.layout.spinner_item,markers);
        imageSpinner.setAdapter(spinnerAdapter);
        imageSpinner.setOnItemSelectedListener(this);
        imageSpinner.setSelection(1);//  TODO захардкодил пока
        freePoints = new LinkedList<>();
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
        int id = item.getItemId();
        switch (id){
            case R.id.ways:{
                intent = new Intent(this,TravelActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.adr_search:{
                intent = new Intent(this,AdrActivity.class);
                startActivityForResult(intent,SEARCH_ADDRESS_CODE);
                break;
            }
            case R.id.add_curr_location:{
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast toast = Toast.makeText(this,"Включите Геолокацию",Toast.LENGTH_LONG);
                    toast.show();

                }
                else {
                    Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    if (location != null) {
                        GeoPoint geoPoint = new GeoPoint(location.getLatitude(),location.getLongitude());
                        overlayItem = new OverlayItem(geoPoint,context.getResources().getDrawable(R.drawable.a));//TODO пока захардкодил иконку
                        currentOverlay.addOverlayItem(overlayItem);//
                        mMapController.getOverlayManager().addOverlay(currentOverlay);//TODO не уверен что нужно добавлять  новый слой
                        freePoints.add(geoPoint);
                    }
                    else {
                        Toast toast = Toast.makeText(this,"Включите Геолокацию",Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
                break;
            }
            case R.id.free_points_show:{
                intent = new Intent(this,FreePointsActivity.class);
                int i = 0;
                for (GeoPoint geoPoint:freePoints){
                    intent.putExtra("lat"+i, geoPoint.getLat());
                    intent.putExtra("lon"+i,geoPoint.getLon());
                    i++;
                }
                intent.putExtra("count",freePoints.size());
                startActivityForResult(intent,FREE_POINTS_CODE);
                break;
            }
            case R.id.add_travel:{
                intent = new Intent(this,AddTravelActivity.class);
                startActivityForResult(intent,ADD_TRAVEL_CODE);
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
                /**
                 * 'элемент слоя=>слой=> карта
                 * Получаю точку, по точке нахожу элемент слоя - удаляю элемент слоя
                 * */
                case FREE_POINTS_CODE:{
                    try {
                        GeoPoint removedPoint = null;
                        OverlayItem removedSl = null;
                        GeoPoint geoPoint = new GeoPoint(data.getDoubleExtra("lat",0.0),data.getDoubleExtra("lon",0.0));
                        for (GeoPoint tmp:freePoints){
                            if (tmp.getLat()==geoPoint.getLat())
                            {
                                removedPoint =tmp;
                            }
                        }
                        freePoints.remove(removedPoint);//удалил из списка свободных точек
                        OverlayItem removedItem = new OverlayItem(removedPoint,null);
                        List<OverlayItem> overlayItems = currentOverlay.getOverlayItems();
                        for (OverlayItem item:overlayItems){
                            if (item.getGeoPoint().getLat()==removedItem.getGeoPoint().getLat()){
                                removedSl = item;
                            }
                        }
                        overlayItems.remove(removedSl);//удалил элемнет слооя(точку с самой карты)
                        UpdateMap();//вернул оставшиеся точки на слой
                    }
                    catch (Exception e){
                        Log.d(TAG,"Ошибка при удалении точки");
                    }
                    break;
                }
                case ADD_TRAVEL_CODE:{
                    break;
                }
                case REMOVE_TRAVEL_CODE:{
                    break;
                }
                default:{
                    break;
                }
            }
        }
    }

    // TODO Сама она не обновляетс после удаления
    public void UpdateMap(){
        currentOverlay.clearOverlayItems();
        OverlayItem overlayItem;
        for (GeoPoint data:freePoints){
            overlayItem = new OverlayItem(data,new BitmapDrawable(getResources(),selectedMarker));
            currentOverlay.addOverlayItem(overlayItem);
        }
    }
    /*
    * отображает на карте только точки путешевствия
    * */
    public void setTravelPoints(Travel travel){
        currentOverlay.clearOverlayItems();
        OverlayItem overlayItem;
        overlayItem = new OverlayItem(travel.getStartPoint(),new BitmapDrawable(getResources(),selectedMarker));
        currentOverlay.addOverlayItem(overlayItem);

        overlayItem = new OverlayItem(travel.getEndPoint(),new BitmapDrawable(getResources(),selectedMarker));
        currentOverlay.addOverlayItem(overlayItem);
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
        overlayManager.addOverlay(new OverlayGeoCode(mapView.getMapController(),getApplicationContext(),currentOverlay,res,freePoints));//подписал контроллер на событие тапа по карте в произв месте
    }

    //TODO методы гуглового kлиента для опред тек положения
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        googleApiClient.connect();
    }
}
