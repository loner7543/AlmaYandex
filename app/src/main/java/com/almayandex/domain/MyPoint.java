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
    private List<Bitmap> photos;
    private String description;

    public MyPoint( GeoPoint geoPoint) {
       photos = new LinkedList<>();
        this.geoPoint = geoPoint;
    }

    public MyPoint(String description, List<Bitmap> photos, GeoPoint geoPoint) {
        this.description = description;
        this.photos = photos;
        this.geoPoint = geoPoint;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public List<Bitmap> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Bitmap> photos) {
        this.photos = photos;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}