import com.google.gson.Gson;

public class Main {
    public static void main(String[] args) {
        Gson gson = new Gson();

        // create
        Person original = new Person("Maiko", "Nadia", 18);
        System.out.println("Original: " + original);

        // convert
        String json = gson.toJson(original);
        System.out.println("JSON: " + json);

        // restore
        Person restored = gson.fromJson(json, Person.class);
        System.out.println("Restored: " + restored);

        // compare
        boolean isEqual = original.equals(restored);
        System.out.println("original.equals(restored) = " + isEqual);
    }
}