import exceptions.*;
import model.*;
import road.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {
    private Bus<Passenger> bus;
    private Taxi<Passenger> taxi;
    private FireTruck fireTruck;
    private PoliceCar policeCar;

    private Passenger passenger1;
    private Passenger passenger2;
    private Firefighter firefighter1;
    private Firefighter firefighter2;
    private Policeman policeman1;
    private Policeman policeman2;

    // виконається перед кожним тестом
    @BeforeEach
    void setUp() {
        bus = new Bus<>(3);
        taxi = new Taxi<>(2);
        fireTruck = new FireTruck(2);
        policeCar = new PoliceCar(2);

        passenger1 = new Passenger("Wrei", "Djkgof", 46);
        passenger2 = new Passenger("Odujfh", "fjisd", 59);
        firefighter1 = new Firefighter("Ldsdiu", "DJjkfd", 23);
        firefighter2 = new Firefighter("Yskdler", "HKjJLK", 12);
        policeman1 = new Policeman("Kdfksf", "JFkfdlk", 34);
        policeman2 = new Policeman("Kvfdkv", "JIlkdj", 56);
    }

    @Test
    void busCanCarryAnyKindOfPassenger() throws VehicleFullException {
        bus.board(passenger1);
        bus.board(firefighter1);
        bus.board(policeman1);

        assertEquals(3, bus.getOccupiedSeats());
    }

    @Test
    void taxiCanCarryAnyKindOfPassenger() throws VehicleFullException {
        taxi.board(passenger1);
        taxi.board(firefighter1);

        assertEquals(2, taxi.getOccupiedSeats());
    }

    @Test
    void fireTruckCanCarryOnlyFirefighters() throws VehicleFullException {
        fireTruck.board(firefighter1);
        fireTruck.board(firefighter2);

        assertEquals(2, fireTruck.getOccupiedSeats());
        // fireTruck.board(passenger1);
    }

    @Test
    void policeCarCanCarryOnlyPolicemen() throws VehicleFullException {
        policeCar.board(policeman1);
        policeCar.board(policeman2);

        assertEquals(2, policeCar.getOccupiedSeats());
        // policeCar.board(firefighter1);
    }

    @Test
    void boardingThrowsWhenVehicleIsFull() throws VehicleFullException {
        taxi.board(passenger1);
        taxi.board(passenger2);

        Passenger extra = new Passenger("Third", "Ikdjfs", 24);
        assertThrows(VehicleFullException.class, () -> taxi.board(extra));
    }

    @Test
    void unboardingRemovesPassenger() throws VehicleFullException, PassengerNotFoundException {
        bus.board(passenger1);
        bus.board(passenger2);

        bus.unboard(passenger1);

        assertEquals(1, bus.getOccupiedSeats());
        assertFalse(bus.getPassengers().contains(passenger1));
    }

    @Test
    void unboardingThrowsWhenPassengerNotInVehicle() {
        assertThrows(PassengerNotFoundException.class, () -> bus.unboard(passenger1));
    }

    @Test
    void roadCountsAllPassengersInAllVehicles() throws VehicleFullException {
        Road road = new Road();

        bus.board(passenger1);
        bus.board(passenger2);
        taxi.board(firefighter1);
        fireTruck.board(firefighter2);
        policeCar.board(policeman1);
        policeCar.board(policeman2);

        road.addCarToRoad(bus);
        road.addCarToRoad(taxi);
        road.addCarToRoad(fireTruck);
        road.addCarToRoad(policeCar);

        assertEquals(6, road.getCountOfHumans());
    }

    @Test
    void roadWithNoVehiclesHasZeroHumans() {
        Road road = new Road();
        assertEquals(0, road.getCountOfHumans());
    }

    @Test
    void boardingNullPassengerThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> bus.board(null));
    }

    @Test
    void vehicleWithInvalidSeatsThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Bus<>(0));
    }
}