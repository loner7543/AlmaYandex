package com.almayandex.almapath;

import android.content.Context;

import com.almayandex.Travel;

import java.util.ArrayList;
import java.util.List;

import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.utils.GeoPoint;

/**
 * Created by Александр on 03.01.2017.
 */

public class OverlayRect extends Overlay {
    OverlayRectItem overlayRectItem;
    Context mContext;
    MapController mMapController;
    RectRender rectRender;
    private Travel travel;

    public OverlayRect(MapController mapController,Travel t) {
        super(mapController);
        mMapController = mapController;
        this.travel = t;
        mContext = mapController.getContext();
        rectRender = new RectRender(travel);
        setIRender(rectRender);
        overlayRectItem = new OverlayRectItem(new GeoPoint(0,0), mContext.getResources().getDrawable(android.R.drawable.btn_star));
        overlayRectItem.geoPoint.add(travel.getStartPoint());
        overlayRectItem.geoPoint.add(travel.getEndPoint());
        addOverlayItem(overlayRectItem);
    }

    public OverlayRect(MapController mapController){
        super(mapController);
    }

    @Override
    public List prepareDraw() {
        ArrayList<OverlayItem> draw = new ArrayList<OverlayItem>();
        overlayRectItem.screenPoint.clear();
        for( GeoPoint point : overlayRectItem.geoPoint){
            overlayRectItem.screenPoint.add(mMapController.getScreenPoint(point));
        }
        draw.add(overlayRectItem);

        return draw;
    }

    public RectRender getRectRender() {
        return rectRender;
    }

    public Travel getTravel() {
        return travel;
    }
}
