package com.almayandex.almapath;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.utils.GeoPoint;
import ru.yandex.yandexmapkit.utils.ScreenPoint;

/**
 * Created by Александр on 03.01.2017.
 */

public class OverlayRectItem extends OverlayItem {
   public   ArrayList<GeoPoint> geoPoint = new ArrayList<GeoPoint>();
    public ArrayList<ScreenPoint> screenPoint = new ArrayList<ScreenPoint>();

    public OverlayRectItem(GeoPoint geoPoint, Drawable drawable) {
        super(geoPoint, drawable);
    }
}
