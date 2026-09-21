package road;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class Road {
    private final List<Vehicle<? extends Passenger>> carsInRoad = new ArrayList<>();

    public void addCarToRoad(Vehicle<? extends Passenger> vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Транспортний засіб не може бути null");
        }
        carsInRoad.add(vehicle);
    }

    public int getCountOfHumans() {
        int count = 0;

        for (Vehicle<? extends Passenger> car : carsInRoad) {
            count += car.getOccupiedSeats();
        }

        return count;
    }
}