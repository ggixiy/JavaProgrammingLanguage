import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translator {

    /**
     * Токенізатор: або слово (літери, можливо з апострофом всередині: don't),
     * або послідовність не-літер (пробіли, розділові знаки).
     * Такий підхід дозволяє зберегти пунктуацію фрази без змін.
     */
    private static final Pattern TOKEN =
            Pattern.compile("\\p{L}+(?:'\\p{L}+)*|[^\\p{L}]+");

    private final Map<String, String> dictionary = new HashMap<>();

    public boolean addWord(String english, String ukrainian) {
        if (english == null || ukrainian == null
                || english.isBlank() || ukrainian.isBlank()) {
            return false;
        }
        dictionary.put(normalize(english), ukrainian.trim());
        return true;
    }

    public String translate(String phrase) {
        if (phrase == null || phrase.isBlank()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        Matcher matcher = TOKEN.matcher(phrase);
        while (matcher.find()) {
            String token = matcher.group();
            if (Character.isLetter(token.charAt(0))) {
                result.append(translateWord(token));
            } else {
                result.append(token);
            }
        }
        return result.toString();
    }

    private String translateWord(String word) {
        String translation = dictionary.get(normalize(word));
        if (translation == null) {
            return "[" + word + "]";
        }
        // Зберігаємо велику літеру на початку слова
        if (Character.isUpperCase(word.charAt(0))) {
            return Character.toUpperCase(translation.charAt(0)) + translation.substring(1);
        }
        return translation;
    }

    private static String normalize(String word) {
        return word.trim().toLowerCase();
    }

    public int size() {
        return dictionary.size();
    }

    public Map<String, String> getDictionary() {
        return Collections.unmodifiableMap(dictionary);
    }
}