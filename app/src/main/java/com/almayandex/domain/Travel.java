package com.almayandex.domain;

/**
 * Created by Александр on 01.01.2017.
 */

public class Travel {
    private MyPoint startPoint;
    private MyPoint endPoint;
    private String title;
    private int color;

    public Travel(MyPoint startPoint, MyPoint endPoint, String title, int c) {
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

    public MyPoint getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(MyPoint endPoint) {
        this.endPoint = endPoint;
    }

    public MyPoint getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(MyPoint startPoint) {
        this.startPoint = startPoint;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
