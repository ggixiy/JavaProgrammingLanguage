package workshop.task_1_1;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by olenasyrota on 6/28/16.
 */
public class AbbrivationBuilder {

    public static String build(List<String> list) {
        StringBuilder result = new StringBuilder();

        list.forEach(word -> {
            if(word != null && !word.isEmpty()) result.append(word.charAt(0));
        });

        return result.toString();
    }

}
