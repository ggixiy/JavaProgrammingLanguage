package forkjoin;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String args[]) {
        final int SIZE = 1_000_000;

        int[] arr = new int[SIZE];
        Random r = new Random();

        for(int i = 0; i < SIZE; i++) {
            arr[i] = r.nextInt(101);
        }

        ForkJoinPool pool = new ForkJoinPool();

        long startTime = System.nanoTime();

        ArraySumTask task = new ArraySumTask(arr, 0, arr.length);

        long result = pool.invoke(task);

        long endTime = System.nanoTime();

        System.out.println("Кількість елементів: " + arr.length);
        System.out.println("Сума: " + result);
        System.out.printf(
                "Час виконання: %.3f мс%n",
                (endTime - startTime) / 1_000_000.0
        );

        pool.shutdown();

//        long expectedSum = 0;
//
//        long startTime2 = System.nanoTime();
//        for (int value : arr) {
//            expectedSum += value;
//        }
//        long endTime2 = System.nanoTime();
//
//
//        System.out.println("Послідовна сума: " + expectedSum);
//        System.out.printf(
//                "Час виконання: %.3f мс%n",
//                (endTime2 - startTime2) / 1_000_000.0
//        );

    }
}
