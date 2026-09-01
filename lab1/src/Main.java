import java.util.Scanner;

public class Main {

    public static void findPerfectNums(int value) {
        int[] perfectNums = new int[value];
        int count = 0;

        for (int num = 1; num <= value; num++) {

            int sum = 0;

            for (int i = 1; i <= num / 2; i++) {
                if (num % i == 0) {
                    sum += i;
                }
            }

            if (sum == num && num != 0) {
                perfectNums[count] = num;
                count++;
            }
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(perfectNums[i]);
            if (i != count - 1) {
                result.append(", ");
            }
        }

        System.out.println("Perfect numbers: " + result);
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter positive integer number: ");
        int value = scanner.nextInt();

        findPerfectNums(value);
    }
}