package forkjoin;

import java.util.Arrays;
import java.util.concurrent.RecursiveTask;

public class ArraySumTask extends RecursiveTask<Long> {

    private final int[] array;
    private final int start;
    private final int end;

    public ArraySumTask(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        if(end - start < 20) {
            long sum = 0;

            for(int i = start; i < end; i++) {
                sum += array[i];
            }

            return sum;
        }

        int mid = (start + end) / 2;

        ArraySumTask leftTask = new ArraySumTask(array, start, mid);
        ArraySumTask rightTask = new ArraySumTask(array, mid, end);

        leftTask.fork(); // запускаємо ліу частину асинхронно

        long rightSum = rightTask.compute(); // праву виконуємо в цьому ж потоці
        long leftSum = leftTask.join(); // резульатат лівої

        return leftSum + rightSum;
    }
}
