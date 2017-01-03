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
       /* overlayRectItem.geoPoint.add(new GeoPoint(55.943048,35.165588));
        overlayRectItem.geoPoint.add(new GeoPoint(56.444277,35.55011));
        overlayRectItem.geoPoint.add(new GeoPoint(56.36525,36.077454));
        overlayRectItem.geoPoint.add(new GeoPoint(56.583692,36.846497));
        overlayRectItem.geoPoint.add(new GeoPoint(56.547372,37.099182));
        overlayRectItem.geoPoint.add(new GeoPoint(56.938981,37.70343));
        overlayRectItem.geoPoint.add(new GeoPoint(56.758746,38.252747));
        overlayRectItem.geoPoint.add(new GeoPoint(56.010666,38.57135));
        overlayRectItem.geoPoint.add(new GeoPoint(55.832144,39.274475));
        overlayRectItem.geoPoint.add(new GeoPoint(55.819802,39.845764));
        overlayRectItem.geoPoint.add(new GeoPoint(55.304138,40.164368));
        overlayRectItem.geoPoint.add(new GeoPoint(54.533833,38.769104));
        overlayRectItem.geoPoint.add(new GeoPoint(54.85764,37.967102));
        overlayRectItem.geoPoint.add(new GeoPoint(54.76267,37.582581));
        overlayRectItem.geoPoint.add(new GeoPoint(55.229023,36.923401));
        overlayRectItem.geoPoint.add(new GeoPoint(55.310391,36.582825));
        overlayRectItem.geoPoint.add(new GeoPoint(55.197683,36.308167));
        overlayRectItem.geoPoint.add(new GeoPoint(55.235288,35.451233));*/
        addOverlayItem(overlayRectItem);
    }

    @Override
    public List prepareDraw() {
        // TODO Auto-generated method stub
        ArrayList<OverlayItem> draw = new ArrayList<OverlayItem>();
        overlayRectItem.screenPoint.clear();
        for( GeoPoint point : overlayRectItem.geoPoint){
            overlayRectItem.screenPoint.add(mMapController.getScreenPoint(point));
        }
        draw.add(overlayRectItem);

        return draw;
    }
}
