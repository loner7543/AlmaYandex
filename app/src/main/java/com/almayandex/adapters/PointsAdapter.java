package com.almayandex.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.almayandex.R;

import java.util.List;

import ru.yandex.yandexmapkit.utils.GeoPoint;

/**
 * Created by Александр on 01.01.2017.
 */

public class PointsAdapter extends BaseAdapter {
    private List<GeoPoint> data;
    private LayoutInflater layoutInflater;
    private Context ctx;
    private  int LayResId;


    public PointsAdapter(Context context, int resource, List<GeoPoint> objects) {
        this.ctx =context;
        this.LayResId = resource;
        this.data = objects;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
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
        GeoPoint point = getCurrentPoint(i);
        TextView lat = (TextView) row.findViewById(R.id.Latitude_text);
        lat.setText(String.valueOf(point.getLat()));

        TextView lon = (TextView) row.findViewById(R.id.Longitude_text);
        lon.setText(String.valueOf(point.getLon()));
        return row;
    }

    public GeoPoint getCurrentPoint(int Position){
        return (GeoPoint) getItem(Position);
    }
}
