import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    // рефлексивність, симетричність, транзитивність, узгодженість, обробку null та порівняння з об'єктом іншого типу
    @Test
    void equalsVerifierTest() {
        EqualsVerifier.forClass(Person.class).usingGetClass().verify();
    }

    @Test
    void personsShouldBeEqual() {
        Person person1 = new Person("Maiko", "Nadia", 30);
        Person person2 = new Person("Maiko", "Nadia", 30);

        assertEquals(person1, person2);
    }

    @Test
    void personsShouldNotBeEqualWhenAgeIsDifferent() {
        Person person1 = new Person("Maiko", "Nadia", 30);
        Person person2 = new Person("Maiko", "Nadia", 25);

        assertNotEquals(person1, person2);
    }

    @Test
    void personsShouldNotBeEqualWhenNameIsDifferent() {
        Person person1 = new Person("Maiko", "Nadia", 30);
        Person person2 = new Person("Maiko", "Oksana", 30);

        assertNotEquals(person1, person2);
    }

    @Test
    void personsShouldNotBeEqualWhenSurnameIsDifferent() {
        Person person1 = new Person("Maiko", "Nadia", 30);
        Person person2 = new Person("Tsapluk", "Nadia", 30);

        assertNotEquals(person1, person2);
    }
}