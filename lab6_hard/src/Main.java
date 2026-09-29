import java.util.*;

public class Main {

    static final Scanner in = new Scanner(System.in);
    static final Random random = new Random();
    static RedBlackTree tree = new RedBlackTree();

    public static void main(String[] args) {
        while (true) {
            System.out.println("""

                    1 — заповнити випадковими числами
                    2 — заповнити впорядкованими числами (за зростанням)
                    3 — ввести числа з клавіатури
                    4 — додати елемент
                    5 — обхід дерева
                    0 — вихід""");
            int choice = readInt("Ваш вибір: ");
            switch (choice) {
                case 1 -> fillRandom(false);
                case 2 -> fillRandom(true);
                case 3 -> fillFromKeyboard();
                case 4 -> addElement();
                case 5 -> traverse();
                case 0 -> { return; }
                default -> System.out.println("Немає такого пункту.");
            }
        }
    }

    static void fillRandom(boolean sorted) {
        int n = readInt("Кількість елементів: ");
        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(100);
        }
        if (sorted) Arrays.sort(array);

        System.out.println("Масив (порядок додавання): " + Arrays.toString(array));
        tree = new RedBlackTree();
        for (int x : array) {
            tree.add(x);
        }
        System.out.println("Дерево:");
        System.out.print(tree);
    }

    static void fillFromKeyboard() {
        System.out.println("Введіть числа через пробіл:");
        String[] parts = in.nextLine().trim().split("\\s+");
        tree = new RedBlackTree();
        for (String p : parts) {
            try {
                tree.add(Integer.parseInt(p));
            } catch (NumberFormatException e) {
                System.out.println("Пропущено: " + p);
            }
        }
        System.out.println("Дерево:");
        System.out.print(tree);
    }

    static void addElement() {
        int x = readInt("Елемент для додавання: ");
        System.out.println(tree.add(x) ? "Додано." : "Такий елемент уже є.");
        System.out.println("Дерево:");
        System.out.print(tree);
    }

    static void traverse() {
        System.out.println("Прямий обхід: " + tree.preOrder());
        System.out.println("Симетричний обхід:  " + tree.inOrder());
    }

    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(in.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Введіть ціле число.");
            }
        }
    }
}