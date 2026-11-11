package com.varshith.namesorter.sorting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.varshith.namesorter.model.PersonName;
import com.varshith.namesorter.parsing.NameParser;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class LastNameThenGivenNamesComparatorTest {

    private final LastNameThenGivenNamesComparator comparator = new LastNameThenGivenNamesComparator();

    @Test
    void sortsByLastName() {
        assertEquals(
                List.of("Marin Alvarez", "Leo Gardner", "Janet Parsons"),
                sorted("Janet Parsons", "Marin Alvarez", "Leo Gardner"));
    }

    @Test
    void lastNameMattersMoreThanGivenNames() {
        assertEquals(
                List.of("Zoe Adams", "Aaron Baker"),
                sorted("Aaron Baker", "Zoe Adams"));
    }

    @Test
    void sortsByGivenNamesWhenLastNamesMatch() {
        assertEquals(
                List.of("Adam Lewis", "Vaughn Lewis", "Zara Lewis"),
                sorted("Vaughn Lewis", "Zara Lewis", "Adam Lewis"));
    }

    @Test
    void comparesLaterGivenNamesWhenEarlierOnesMatch() {
        assertEquals(
                List.of("Beau Aaron Bentley", "Beau Tristan Bentley"),
                sorted("Beau Tristan Bentley", "Beau Aaron Bentley"));
    }

    @Test
    void putsTheNameWithFewerGivenNamesFirstWhenTheRestMatch() {
        assertEquals(
                List.of("Leo Gardner", "Leo Adam Gardner"),
                sorted("Leo Adam Gardner", "Leo Gardner"));
    }

    @Test
    void treatsUpperAndLowerCaseAsEqual() {
        assertEquals(
                List.of("Ann mcdonald", "Bob McDonald", "Cy MCDONALD"),
                sorted("Cy MCDONALD", "Ann mcdonald", "Bob McDonald"));
    }

    @Test
    void identicalNamesAreEqual() {
        assertEquals(0, comparator.compare(name("Janet Parsons"), name("Janet Parsons")));
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
