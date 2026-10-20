package task1;

import java.util.concurrent.atomic.AtomicInteger;

public class Account {
    private static final AtomicInteger nextId = new AtomicInteger(1);

    private final int id;
    private int balance;

    public Account(int balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Початковий баланс не може бути від'ємним");
        }

        this.id = nextId.getAndIncrement();
        this.balance = balance;
    }

    public synchronized void withdraw(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Сума зняття має бути додатною"
            );
        }

        if (amount > balance) {
            throw new IllegalStateException(
                    "Недостатньо коштів на рахунку"
            );
        }

        balance -= amount;
    }

    public synchronized void deposit(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сума поповнення має бути додатною");
        }
        balance += amount;
    }

    public synchronized int getBalance() {
        return balance;
    }

    public int getId() {
        return id;
    }
}