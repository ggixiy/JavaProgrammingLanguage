package model;

public class Taxi<T extends Passenger> extends Car<T> {
    private static final int DEFAULT_SEATS = 4;

    public Taxi() {
        super(DEFAULT_SEATS);
    }

    public Taxi(int seats) {
        super(seats);
    }
}

