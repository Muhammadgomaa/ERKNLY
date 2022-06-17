package com.example.erknly;


public class CarDriver extends User  {


    private String License,Type;


    public String getLicense() {
        return License;
    }

    public void setLicense(String license) {
        License = license;
    }

    public String getType() {
        return Type;
    }

    public void setType() {
        Type = "Car Driver";
    }
}
