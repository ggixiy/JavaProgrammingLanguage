import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {

    public static void findPerfectNums(int value) {

        IntStream.rangeClosed(1, value)
                .filter(num ->
                        IntStream.rangeClosed(1, num / 2)
                                .filter(i -> num % i == 0)
                                .sum() == num
                )
                .forEach(num -> System.out.print(num + " "));

        System.out.println();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter positive integer number:");
        int value = scanner.nextInt();

        findPerfectNums(value);
    }
}