package model;

public class FireTruck extends Car<Firefighter> {
    private static final int DEFAULT_SEATS = 6;

    public FireTruck() {
        super(DEFAULT_SEATS);
    }

    public FireTruck(int maxSeats) {
        super(maxSeats);
    }
}