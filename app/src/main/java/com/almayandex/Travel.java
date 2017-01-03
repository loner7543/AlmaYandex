package com.almayandex;

import ru.yandex.yandexmapkit.utils.GeoPoint;

/**
 * Created by Александр on 01.01.2017.
 */

public class Travel {
    private GeoPoint startPoint;
    private GeoPoint endPoint;
    private String title;
    private int color;

    public Travel(GeoPoint startPoint, GeoPoint endPoint, String title, int c) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.title = title;
        this.color = c;
    }

    public Travel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public GeoPoint getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(GeoPoint endPoint) {
        this.endPoint = endPoint;
    }

    public GeoPoint getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(GeoPoint startPoint) {
        this.startPoint = startPoint;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
