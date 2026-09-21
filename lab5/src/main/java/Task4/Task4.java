package Task4;

import java.io.*;
import java.net.URI;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task4 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Введіть URL: ");
        String url = sc.nextLine().trim();

        try {
            String html = download(url);
            Map<String, Integer> counts = countTags(html);

            System.out.println("\nЗа алфавітом");
            new TreeMap<>(counts).forEach((tag, n) -> System.out.println(tag + ": " + n));

            System.out.println("\nЗа частотою");
            List<Map.Entry<String, Integer>> list = new ArrayList<>(counts.entrySet());
            list.sort(Map.Entry.<String, Integer>comparingByValue()
                    .thenComparing(Map.Entry.comparingByKey()));
            for (Map.Entry<String, Integer> e : list) {
                System.out.println(e.getKey() + ": " + e.getValue());
            }
        } catch (IllegalArgumentException | IOException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    private static String download(String url) throws IOException {
        URLConnection conn = URI.create(url).toURL().openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }

    private static Map<String, Integer> countTags(String html) {
        Map<String, Integer> counts = new HashMap<>();
        Matcher m = Pattern.compile("<([a-zA-Z][a-zA-Z0-9]*)").matcher(html);
        while (m.find()) {
            counts.merge(m.group(1).toLowerCase(), 1, Integer::sum);
        }
        return counts;
    }
}