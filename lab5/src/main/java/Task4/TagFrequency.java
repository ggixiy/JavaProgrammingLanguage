package Task4;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagFrequency {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть URL: ");
        String url = scanner.nextLine().trim();

        String html;
        try {
            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .build();
            HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
            html = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (Exception e) {
            System.out.println("Не вдалося завантажити сторінку: " + e.getMessage());
            return;
        }

        Map<String, Integer> frequency = new HashMap<>();
        Matcher m = Pattern.compile("<([a-zA-Z][a-zA-Z0-9]*)").matcher(html);
        while (m.find()) {
            frequency.merge(m.group(1).toLowerCase(), 1, Integer::sum);
        }

        System.out.println("\nВ лексикографічному порядку ");
        new TreeMap<>(frequency).forEach(
                (tag, count) -> System.out.printf("%-15s %d%n", tag, count));

        System.out.println("\nЗа зростанням частоти ");
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(frequency.entrySet());
        entries.sort(Map.Entry.<String, Integer>comparingByValue()
                .thenComparing(Map.Entry.comparingByKey()));
        for (Map.Entry<String, Integer> e : entries) {
            System.out.printf("%-15s %d%n", e.getKey(), e.getValue());
        }
    }
}