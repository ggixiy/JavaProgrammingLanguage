package task2;

public class RingBuffer<T> {

    private static class Node<T> {
        T value;
        Node<T> next;
    }

    private Node<T> head;
    private Node<T> tail;

    public RingBuffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive.");
        }

        // capacity + 1 вузол (один завжди вільний, щоб відрізнити повний буфер від порожнього)
        Node<T> first = new Node<>();
        Node<T> current = first;
        for (int i = 0; i < capacity; i++) {
            current.next = new Node<>();
            current = current.next;
        }
        current.next = first; // замикаємо список у кільце

        head = first;
        tail = first;
    }

    private boolean isEmpty() {
        return head == tail;
    }

    private boolean isFull() {
        return tail.next == head;
    }

    public synchronized void put(T item) throws InterruptedException {
        while (isFull()) {
            wait();
        }
        tail.value = item;
        tail = tail.next;
        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        while (isEmpty()) {
            wait();
        }
        T item = head.value;
        head.value = null;
        head = head.next;
        notifyAll();
        return item;
    }
}
