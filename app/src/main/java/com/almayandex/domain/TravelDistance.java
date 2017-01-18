package com.almayandex.domain;

/**
 * Created by User on 017 17.01.17.
 */

public class TravelDistance {
    private Travel travel;
    private  double distance;

    public TravelDistance(Travel travel, double distance) {
        this.travel = travel;
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Travel getTravel() {
        return travel;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
    }
}
