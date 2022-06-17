package com.example.erknly;

public class Garage {

    private String GarageName,Address;
    private int NumberofSlots,NumberofAvailable;
    private double RatePerHour,X_CoordGoogleMap,Y_CoordGoogleMap;

    public double getX_CoordGoogleMap() {
        return X_CoordGoogleMap;
    }

    public void setX_CoordGoogleMap(double x_CoordGoogleMap) {
        X_CoordGoogleMap = x_CoordGoogleMap;
    }

    public double getY_CoordGoogleMap() {
        return Y_CoordGoogleMap;
    }

    public void setY_CoordGoogleMap(double y_CoordGoogleMap) {
        Y_CoordGoogleMap = y_CoordGoogleMap;
    }

    public int getNumberofAvailable() {
        return NumberofAvailable;
    }

    public void setNumberofAvailable(int numberofAvailable) {
        NumberofAvailable = numberofAvailable;
    }

    public String getGarageName() {
        return GarageName;
    }

    public void setGarageName(String garageName) {
        GarageName = garageName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getNumberofSlots() {
        return NumberofSlots;
    }

    public void setNumberofSlots(int numberofSlots) {
        NumberofSlots = numberofSlots;
    }

    public double getRatePerHour() {
        return RatePerHour;
    }

    public void setRatePerHour(double ratePerHour) {
        RatePerHour = ratePerHour;
    }
}
