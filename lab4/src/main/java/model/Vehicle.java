package model;

import exceptions.PassengerNotFoundException;
import exceptions.VehicleFullException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Vehicle<T extends Passenger> {
    private final List<T> passengers = new ArrayList<>();
    private final int maxSeats;

    public Vehicle(int maxSeats) {
        if (maxSeats <= 0) {
            throw new IllegalArgumentException("Кількість місць має бути додатною");
        }
        this.maxSeats = maxSeats;
    }

    public int getMaxSeats() {
        return maxSeats;
    }

    public int getOccupiedSeats() {
        return passengers.size();
    }

    public List<T> getPassengers() {
        return Collections.unmodifiableList(passengers);
    }

    public void board(T passenger) throws VehicleFullException {
        if (passenger == null) {
            throw new IllegalArgumentException("Пасажир не може бути null");
        }
        if (getOccupiedSeats() >= maxSeats) {
            throw new VehicleFullException(
                    "Немає вільних місць у " + getClass().getSimpleName()
                            + " (максимум " + maxSeats + " місць)");
        }
        passengers.add(passenger);
    }

    public void unboard(T passenger) {
        if (!passengers.remove(passenger)) {
            throw new PassengerNotFoundException(
                    "Пасажир " + passenger + " не перебуває у "
                            + getClass().getSimpleName());
        }
    }
}
