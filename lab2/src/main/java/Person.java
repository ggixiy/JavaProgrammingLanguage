import java.util.Objects;

public class Person {
    private final String lastName;
    private final String firstName;
    private final int age;

    public Person(String lastName, String firstName, int age) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.age = age;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        // same address
        if (this == o) {
            return true;
        }

        // тільки клас Person без врахування нащадків
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Person person = (Person) o;

        return age == person.age
                && Objects.equals(lastName, person.lastName)
                && Objects.equals(firstName, person.firstName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastName, firstName, age);
    }

    @Override
    public String toString() {
        return "lastName = " + lastName +
                ", firstName = " + firstName +
                ", age = " + age;
    }
}
