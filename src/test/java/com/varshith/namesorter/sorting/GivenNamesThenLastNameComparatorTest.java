package com.varshith.namesorter.sorting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.varshith.namesorter.model.PersonName;
import com.varshith.namesorter.parsing.NameParser;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class GivenNamesThenLastNameComparatorTest {

    private final GivenNamesThenLastNameComparator comparator = new GivenNamesThenLastNameComparator();

    @Test
    void sortsTheInterviewExample() {
        assertEquals(
                List.of("a b", "a b c", "a c b", "b a", "c b"),
                sorted("b a", "c b", "a c b", "a b c", "a b"));
    }

    @Test
    void sortsByFirstGivenName() {
        assertEquals(
                List.of("Janet Parsons", "Leo Gardner", "Marin Alvarez"),
                sorted("Marin Alvarez", "Janet Parsons", "Leo Gardner"));
    }

    @Test
    void givenNamesMatterMoreThanLastName() {
        assertEquals(
                List.of("Aaron Zane", "Zoe Adams"),
                sorted("Zoe Adams", "Aaron Zane"));
    }

    @Test
    void comparesLaterGivenNamesWhenEarlierOnesMatch() {
        assertEquals(
                List.of("Hunter Uriah Adam Clarke", "Hunter Uriah Mathew Clarke"),
                sorted("Hunter Uriah Mathew Clarke", "Hunter Uriah Adam Clarke"));
    }

    @Test
    void putsAMissingGivenNameBeforeAPresentOne() {
        assertEquals(
                List.of("Leo Zimmer", "Leo Adam Gardner"),
                sorted("Leo Adam Gardner", "Leo Zimmer"));
    }

    @Test
    void sortsByLastNameWhenGivenNamesMatch() {
        assertEquals(
                List.of("Vaughn Adams", "Vaughn Lewis"),
                sorted("Vaughn Lewis", "Vaughn Adams"));
    }

    @Test
    void treatsUpperAndLowerCaseAsEqual() {
        assertEquals(
                List.of("ann Smith", "Bob Smith", "CY Smith"),
                sorted("CY Smith", "ann Smith", "Bob Smith"));
    }

    @Test
    void identicalNamesAreEqual() {
        assertEquals(0, comparator.compare(name("Janet Parsons"), name("JANET parsons")));
    }

    private List<String> sorted(String... fullNames) {
        return Stream.of(fullNames)
                .map(this::name)
                .sorted(comparator)
                .map(PersonName::fullName)
                .toList();
    }

    private PersonName name(String fullName) {
        return new NameParser().parse(fullName);
    }
}
