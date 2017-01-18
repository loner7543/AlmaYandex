package com.almayandex.domain;

import android.graphics.Bitmap;

import java.util.LinkedList;
import java.util.List;

import ru.yandex.yandexmapkit.utils.GeoPoint;

/**
 * Created by User on 017 17.01.17.
 */

public class MyPoint {
    private GeoPoint geoPoint;
    private Bitmap photo;
    private String description;

    public MyPoint( GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public MyPoint(String description, Bitmap photos, GeoPoint geoPoint) {
        this.description = description;
        this.photo = photos;
        this.geoPoint = geoPoint;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public Bitmap getPhotos() {
        return photo;
    }

    public void setPhotos(Bitmap photos) {
        this.photo = photos;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}