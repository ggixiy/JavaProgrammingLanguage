import java.util.Locale;
import java.util.SplittableRandom;
import java.util.concurrent.atomic.AtomicLong;

public class ParallelMonteCarloPi {

    /** Загальна кількість «кинутих» точок (не залежить від кількості потоків). */
    private static final long TOTAL_ITERATIONS = 1_000_000_000L;

    /** Фіксоване зерно: дозволяє відтворювати результат між запусками. */
    private static final long SEED = 42L;

    /** Спільний лічильник точок у колі; кожен потік додає до нього результат один раз. */
    private static final AtomicLong INSIDE_CIRCLE = new AtomicLong();

    /** Робота одного потоку: кидає свою частку точок і рахує влучання в коло. */
    private static final class Worker extends Thread {
        private final long iterations;
        private final SplittableRandom random; // не потокобезпечний, тому в кожного потоку свій

        Worker(long iterations, SplittableRandom random) {
            this.iterations = iterations;
            this.random = random;
        }

        @Override
        public void run() {
            long inside = 0;                       // локальний лічильник — без синхронізації в циклі
            for (long i = 0; i < iterations; i++) {
                double x = random.nextDouble();
                double y = random.nextDouble();
                if (x * x + y * y <= 1.0) {
                    inside++;
                }
            }
            INSIDE_CIRCLE.addAndGet(inside);       // одна атомарна операція в кінці роботи
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1) {
            System.err.println("Використання: java ParallelMonteCarloPi <кількість_потоків>");
            System.exit(1);
        }

        int threads;
        try {
            threads = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Кількість потоків має бути цілим числом: " + args[0]);
            System.exit(1);
            return;
        }
        if (threads < 1) {
            System.err.println("Кількість потоків має бути додатною.");
            System.exit(1);
        }

        // Ділимо ітерації порівну; залишок розподіляємо по одній першим потокам,
        // щоб сума завжди дорівнювала TOTAL_ITERATIONS.
        long base = TOTAL_ITERATIONS / threads;
        long remainder = TOTAL_ITERATIONS % threads;

        // Незалежні генератори для потоків створюємо в головному потоці від одного зерна.
        SplittableRandom master = new SplittableRandom(SEED);
        Worker[] workers = new Worker[threads];
        for (int t = 0; t < threads; t++) {
            long share = base + (t < remainder ? 1 : 0);
            workers[t] = new Worker(share, master.split());
        }

        long start = System.nanoTime();

        for (Worker w : workers) {
            w.start();
        }
        for (Worker w : workers) {
            w.join();                              // чекаємо завершення всіх потоків
        }

        long elapsedNanos = System.nanoTime() - start;

        double pi = 4.0 * INSIDE_CIRCLE.get() / TOTAL_ITERATIONS;

        System.out.printf(Locale.US, "PI is %.5f%n", pi);
        System.out.printf(Locale.US, "THREADS %d%n", threads);
        System.out.printf(Locale.US, "ITERATIONS %,d%n", TOTAL_ITERATIONS);
        System.out.printf(Locale.US, "TIME %.2fms%n", elapsedNanos / 1_000_000.0);
    }
}