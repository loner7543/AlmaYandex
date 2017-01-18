package com.almayandex.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.almayandex.R;
import com.almayandex.domain.MyPoint;
import com.almayandex.domain.Travel;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Александр on 03.01.2017.
 */

public class TravelAdapter extends BaseAdapter {
    private List<Travel> data;
    private LayoutInflater layoutInflater;
    private Context ctx;
    private  int LayResId;
    private Geocoder geocoder;
    private List<Address> fromAaddress;
    private Address fromAddr;
    private List<Address> toAddressList;
    private Address toAddress;
    private List<MyPoint> dbPoints;

    public TravelAdapter(Context context, int resource, List<Travel> objects, List<MyPoint> dbData) {
        this.ctx =context;
        this.LayResId = resource;
        this.data = objects;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        geocoder = new Geocoder(ctx, Locale.getDefault());
        this.dbPoints = dbData;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        Travel currMeet = getTravel(i);
        row = layoutInflater.inflate(LayResId,viewGroup,false);
        try {
            fromAaddress = geocoder.getFromLocation(currMeet.getStartPoint().getGeoPoint().getLat(),currMeet.getStartPoint().getGeoPoint().getLon(),1);
            fromAddr = fromAaddress.get(0);

            toAddressList = geocoder.getFromLocation(currMeet.getEndPoint().getGeoPoint().getLat(),currMeet.getEndPoint().getGeoPoint().getLon(),1);
            toAddress = toAddressList.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextView nameText = (TextView) row.findViewById(R.id.travel_name);
        nameText.setText(currMeet.getTitle());

        TextView startPointText = (TextView) row.findViewById(R.id.start_text_point);
        startPointText.setText(fromAddr.getAdminArea()+"  ,"+fromAddr.getCountryName()+"  ,"+fromAddr.getAdminArea()+fromAddr.getSubThoroughfare());

        TextView endPointText = (TextView) row.findViewById(R.id.end_text_point);
        endPointText.setText(toAddress.getAdminArea()+"  ,"+toAddress.getCountryName()+"  ,"+toAddress.getAdminArea()+toAddress.getSubThoroughfare());
        LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.travelPhotos);
        for (MyPoint myPoint:dbPoints){
            //фотка для точки начала пут
            if (currMeet.getStartPoint().getGeoPoint().getLat()==myPoint.getGeoPoint().getLat()){
                ImageView imageView = new ImageView(ctx);
                imageView.setImageBitmap(myPoint.getPhotos().get(0));
                imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.addView(imageView);
            }

            if (currMeet.getEndPoint().getGeoPoint().getLat()==myPoint.getGeoPoint().getLat()){
                ImageView imageView = new ImageView(ctx);
                imageView.setImageBitmap(myPoint.getPhotos().get(0));
                imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.addView(imageView);
            }
        }
        return row;
    }

    public Travel getTravel(int Position){
        return (Travel) getItem(Position);
    }
}
