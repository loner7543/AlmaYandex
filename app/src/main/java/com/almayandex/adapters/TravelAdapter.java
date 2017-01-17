package com.almayandex.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.almayandex.R;
import com.almayandex.Travel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
    private GridView gridView;

    public TravelAdapter(Context context, int resource, List<Travel> objects) {
        this.ctx =context;
        this.LayResId = resource;
        this.data = objects;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        geocoder = new Geocoder(ctx, Locale.getDefault());
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
        Travel currMeet = getPhoto(i);
        List<Bitmap> photos = currMeet.getStartPoint().getPhotos();
        photos.addAll(currMeet.getEndPoint().getPhotos());
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
        gridView = (GridView) row.findViewById(R.id.gridView);
        for (Bitmap bitmap:photos){
            ImageView imageView = (ImageView) gridView.findViewById(R.id.elem);
            imageView.setImageBitmap(bitmap);
            gridView.addView(imageView);
        }
        return row;
    }

    public Travel getPhoto(int Position){
        return (Travel) getItem(Position);
    }
}
