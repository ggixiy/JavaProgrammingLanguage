package task1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class TransferTest {
    private static final int ACCOUNTS_SIZE = 300;
    private static final int THREADS_COUNT = 2000;
    private static final int TRANSFERS_PER_THREAD = 100;

    private final Bank bank = new Bank();
    private final List<Account> accounts = new ArrayList<>();
    private final Random r = new Random();
    private long initialSum = 0;

    @BeforeEach
    public void setUpAccounts() {
        for (int i = 0; i < ACCOUNTS_SIZE; i++) {
            int balance = r.nextInt(1_000_000 + 1);
            accounts.add(new Account(balance));
            initialSum += balance;
        }
    }

    @Test
    public void concurrentTransfersPreserveTotalSum() throws InterruptedException {
        CountDownLatch startGate = new CountDownLatch(1);
        AtomicInteger successful = new AtomicInteger();
        AtomicInteger rejected = new AtomicInteger();
        ConcurrentLinkedQueue<Throwable> errors = new ConcurrentLinkedQueue<>();

        Runnable task = () -> {
            try {
                startGate.await(); // чекаємо, поки всі потоки будуть готові
                ThreadLocalRandom rnd = ThreadLocalRandom.current();
                for (int i = 0; i < TRANSFERS_PER_THREAD; i++) {
                    Account from = accounts.get(rnd.nextInt(accounts.size()));
                    Account to = accounts.get(rnd.nextInt(accounts.size()));
                    int amount = rnd.nextInt(1, 10_000);

                    if (bank.transfer(from, to, amount)) {
                        successful.incrementAndGet();
                    } else {
                        rejected.incrementAndGet();
                    }
                }
            } catch (Throwable e) {
                errors.add(e);
            }
        };

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < THREADS_COUNT; i++) {
            Thread t = new Thread(task);
            threads.add(t);
            t.start();
        }

        startGate.countDown();

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("Успішних переказів: " + successful.get());
        System.out.println("Відхилених переказів: " + rejected.get());
        System.out.println("Сума до: " + initialSum + ", після: " + countSum());

        assertTrue(errors.isEmpty(), "Помилки в потоках: " + errors);
        assertEquals(initialSum, countSum(), "Сума грошей у банку змінилась");
        for (Account account : accounts) {
            assertTrue(account.getBalance() >= 0,
                    "Від'ємний баланс у рахунку " + account.getId());
        }
    }

    private long countSum() {
        long sum = 0;
        for (Account account : accounts) {
            sum += account.getBalance();
        }
        return sum;
    }
}