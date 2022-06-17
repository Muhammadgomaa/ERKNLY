package com.example.erknly;

public class Status {

    int NumberofReservations;
    long TotalTimeofReservations;
    double TotalCostofReservations;


    public int getNumberofReservations() {
        return NumberofReservations;
    }

    public void setNumberofReservations(int numberofReservations) {
        NumberofReservations = numberofReservations;
    }

    public long getTotalTimeofReservations() {
        return TotalTimeofReservations;
    }

    public void setTotalTimeofReservations(long totalTimeofReservations) {
        TotalTimeofReservations = totalTimeofReservations;
    }

    public double getTotalCostofReservations() {
        return TotalCostofReservations;
    }

    public void setTotalCostofReservations(double totalCostofReservations) {
        TotalCostofReservations = totalCostofReservations;
    }
}
