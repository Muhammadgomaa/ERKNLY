package com.example.erknly;

public class GarageOwner extends User {

    private String PersonalID,Type;

    public String getPersonalID() {
        return PersonalID;
    }

    public void setPersonalID(String personalID) {
        PersonalID = personalID;
    }

    public String getType() {
        return Type;
    }

    public void setType() {
        Type = "Garage Owner";
    }
}
