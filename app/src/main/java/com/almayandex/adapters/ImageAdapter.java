package com.almayandex.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.almayandex.R;

import java.util.List;

/**
 * Created by Александр on 31.12.2016.
 */

public class ImageAdapter extends BaseAdapter {
    private List<Bitmap> data;
    private LayoutInflater layoutInflater;
    private Context ctx;
    private  int LayResId;


    public ImageAdapter(Context context, int resource, List<Bitmap> objects) {
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
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        row = layoutInflater.inflate(LayResId,viewGroup,false);
        Bitmap currMeet = getPhoto(i);
        ImageView meetName = (ImageView) row.findViewById(R.id.image_item);
        meetName.setImageBitmap(currMeet);
        return row;
    }

    public Bitmap getPhoto(int Position){
        return (Bitmap) getItem(Position);
    }
}
