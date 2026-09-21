package model;

public class Car<T extends Passenger> extends Vehicle<T> {

    public Car(int maxSeats) {
        super(maxSeats);
    }
}