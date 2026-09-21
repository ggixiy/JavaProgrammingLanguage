package model;

public class Bus<T extends Passenger> extends Vehicle<T> {
    private static final int DEFAULT_SEATS = 30;

    public Bus() {
        super(DEFAULT_SEATS);
    }

    public Bus(int seats) {
        super(seats);
    }
}
