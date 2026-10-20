package task2;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        RingBuffer<String> first = new RingBuffer<>(10);
        RingBuffer<String> second = new RingBuffer<>(10);

        for (int i = 1; i <= 5; i++) {
            int threadNo = i;
            Thread t = new Thread(() -> {
                int msgNo = 0;
                try {
                    while (true) {
                        msgNo++;
                        first.put("Потік №" + threadNo + " згенерував повідомлення " + msgNo);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            t.setDaemon(true);
            t.start();
        }

        for (int i = 1; i <= 2; i++) {
            int threadNo = i;
            Thread t = new Thread(() -> {
                try {
                    while (true) {
                        String msg = first.take();
                        second.put("Потік №" + threadNo + " переклав повідомлення " + msg);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            t.setDaemon(true);
            t.start();
        }

        for (int i = 0; i < 100; i++) {
            String msg = second.take();
            System.out.println(msg);
        }
    }
}
