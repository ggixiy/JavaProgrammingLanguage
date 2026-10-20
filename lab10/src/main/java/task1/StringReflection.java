package task1;

import java.lang.reflect.Field;
import java.util.Scanner;

public class StringReflection {

    static void replaceValue(String target, String replacement) throws ReflectiveOperationException {
        Field valueField = String.class.getDeclaredField("value");
        valueField.setAccessible(true);

        Field coderField = String.class.getDeclaredField("coder");
        coderField.setAccessible(true);

        valueField.set(target, valueField.get(replacement));
        coderField.set(target, coderField.get(replacement));

        Field hashField = String.class.getDeclaredField("hash");
        hashField.setAccessible(true);
        hashField.setInt(target, 0);
    }

    public static void main(String[] args) throws ReflectiveOperationException {
        Scanner scanner = new Scanner(System.in);

        // 1) Рядок-літерал
        String literal = "Hello";
        String sameLiteral = "Hello"; // для демонстрації string pool

        System.out.println("Літерал до заміни: " + literal);
        System.out.println("Копія літералу, для якої не будемо викликати replaceValue(): " + sameLiteral);

        System.out.print("Введіть заміну для літерала: ");
        String literalReplacement = scanner.nextLine();

        replaceValue(literal, literalReplacement);

        System.out.println("Літерал після заміни: " + literal);
        System.out.println("Копія літералу, для якої не викликали replaceValue():  " + sameLiteral);

        // 2) Рядок з клавіатури
        System.out.print("Введіть рядок: ");
        String input = scanner.nextLine();

        System.out.print("Введіть заміну для рядку: ");
        String inputReplacement = scanner.nextLine();

        System.out.println("Рядок до заміни: " + input);
        replaceValue(input, inputReplacement);
        System.out.println("Рядок після заміни: " + input);
    }
}

//    --add-opens java.base/java.lang=ALL-UNNAMED