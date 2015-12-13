package com.wekan.model;

import android.location.Location;

/**
 * Created by yuanyuan06 on 2015/12/2.
 */
public class Position {
    public double x;
    public double y;
    public double z;

    public Position() {
    }

    public Position(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position(Location location) {
        if (null == location) return;
        x = location.getLongitude();
        y = location.getLatitude();
        z = location.getAltitude();
    }

    @Override
    public String toString() {
        return "x:" + x + " y:" + y + " z:" + z;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

}
