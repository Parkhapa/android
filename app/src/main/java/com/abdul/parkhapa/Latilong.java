package com.abdul.parkhapa;

/**
 * Created by abduljama on 4/5/18.
 */

public class Latilong {
  double latitude;
  double longitude;

    public Latilong() {
    }


    public Latilong(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
