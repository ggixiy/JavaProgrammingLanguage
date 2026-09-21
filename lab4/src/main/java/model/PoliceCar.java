package model;

public class PoliceCar extends Car<Policeman> {
    private static final int DEFAULT_SEATS = 4;

    public PoliceCar() {
        super(DEFAULT_SEATS);
    }

    public PoliceCar(int maxSeats) {
        super(maxSeats);
    }
}