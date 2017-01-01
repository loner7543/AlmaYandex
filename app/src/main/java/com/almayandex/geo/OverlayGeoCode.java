package com.almayandex.geo;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;

import com.almayandex.R;

import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.map.GeoCode;
import ru.yandex.yandexmapkit.map.GeoCodeListener;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.utils.GeoPoint;
import ru.yandex.yandexmapkit.utils.ScreenPoint;

/**
 * Создает
 */

public class OverlayGeoCode extends Overlay implements GeoCodeListener {
    private Overlay overlay;
    private OverlayItem overlayItem;
    private MapController controller;
    private Context ctx;
    private BitmapDrawable bitmapDrawable;

    public OverlayGeoCode(MapController mapController, Context context,Overlay ov,BitmapDrawable drawable) {
        super(mapController);
        this.controller = mapController;
        this.ctx = context;
        this.overlay = ov;
        this.bitmapDrawable = drawable;
    }


    @Override
    public boolean onSingleTapUp(float v, float v1) {
        getMapController().getDownloader().getGeoCode(this, getMapController().getGeoPoint(new ScreenPoint(v, v1)));
        return  true;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public boolean onFinishGeoCode(final GeoCode geoCode) {
        if (geoCode != null){
            getMapController().getMapView().post(new Runnable() {
                @Override
                public void run() {
                    GeoPoint geoPoint = geoCode.getGeoPoint();
                    overlayItem = new OverlayItem(geoPoint,bitmapDrawable);
                    overlay.addOverlayItem(overlayItem);
                    controller.getOverlayManager().addOverlay(overlay);
                }
            });
        }
        return true;
    }
    }
