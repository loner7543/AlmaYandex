package com.almayandex.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.almayandex.DbUtils;
import com.almayandex.domain.MyPoint;
import com.almayandex.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ru.yandex.yandexmapkit.utils.GeoPoint;

/**
 * Created by Александр on 01.01.2017.
 */

public class PointsAdapter extends BaseAdapter {
    private List<MyPoint> data;
    private LayoutInflater layoutInflater;
    private Context ctx;
    private  int LayResId;
    private Geocoder geocoder;
    private List<Address> address;
    private Address addr;
    private SharedPreferences shre;
    private List<MyPoint> DbPoints;


    public PointsAdapter(Context context, int resource, List<MyPoint> objects,List<MyPoint> dbData) {
        this.ctx =context;
        this.LayResId = resource;
        this.data = objects;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        geocoder = new Geocoder(ctx, Locale.getDefault());
        shre = PreferenceManager.getDefaultSharedPreferences(ctx);
        this.DbPoints = dbData;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public MyPoint getItem(int i) {
        return  data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        row = layoutInflater.inflate(LayResId,viewGroup,false);
        MyPoint myPoint = getCurrentPoint(i);
        GeoPoint point = getCurrentPoint(i).getGeoPoint();
        try {
            address = geocoder.getFromLocation(point.getLat(),point.getLon(),1);
            addr = address.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        TextView lat = (TextView) row.findViewById(R.id.Latitude_text);
        TextView lon = (TextView) row.findViewById(R.id.Longitude_text);
        TextView descText = (TextView) row.findViewById(R.id.point_description);
        for (MyPoint p:DbPoints){
            if (p.getGeoPoint().getLat()==myPoint.getGeoPoint().getLat()){
                descText.setText(p.getDescription());
            }
        }
        String adminArea = addr.getAdminArea();
        if (adminArea==null)
        {
            lat.setText(addr.getCountryName());
        }
        else {
            lat.setText(addr.getCountryName()+" , "+addr.getAdminArea());
        }
        String street = addr.getSubThoroughfare();
        if (street==null){
            lon.setText(addr.getThoroughfare()+", 0");
        }
        else {
            lon.setText(addr.getThoroughfare()+", "+addr.getSubThoroughfare());
        }

        for (MyPoint p:DbPoints){
            if (myPoint.getGeoPoint().getLat()==p.getGeoPoint().getLat()){
                LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.photos_layout);
                ImageView imageView = new ImageView(ctx);
                imageView.setImageBitmap(p.getPhotos());
                imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.addView(imageView);
            }
        }

        return row;
    }

    public MyPoint getCurrentPoint(int Position){
        return (MyPoint) getItem(Position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    public List<MyPoint> getData() {
        return data;
    }

    public List<MyPoint> getDbPoints() {
        return DbPoints;
    }
}
