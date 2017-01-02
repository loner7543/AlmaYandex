package com.almayandex;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.overlay.balloon.OnBalloonListener;
import ru.yandex.yandexmapkit.utils.GeoPoint;

/**
 * Created by Александр on 02.01.2017.
 */

public class BallonItem extends BalloonItem implements OnBalloonListener {
    public ImageView imageView;
    public TextView textView;
    public Context mContext;

    public BallonItem(Context context, GeoPoint geoPoint) {
        super(context, geoPoint);
        mContext = context;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public void onBalloonViewClick(BalloonItem balloonItem, View view) {

    }

    @Override
    public void onBalloonShow(BalloonItem balloonItem) {

    }

    @Override
    public void onBalloonHide(BalloonItem balloonItem) {

    }

    @Override
    public void onBalloonAnimationStart(BalloonItem balloonItem) {

    }

    @Override
    public void onBalloonAnimationEnd(BalloonItem balloonItem) {

    }
}
