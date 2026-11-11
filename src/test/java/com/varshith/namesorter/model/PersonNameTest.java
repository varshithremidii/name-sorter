package com.varshith.namesorter.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class PersonNameTest {

    @Test
    void acceptsOneGivenName() {
        PersonName name = new PersonName(List.of("Janet"), "Parsons");

        assertEquals(List.of("Janet"), name.givenNames());
        assertEquals("Parsons", name.lastName());
    }

    @Test
    void acceptsThreeGivenNames() {
        PersonName name = new PersonName(List.of("Hunter", "Uriah", "Mathew"), "Clarke");

        assertEquals(List.of("Hunter", "Uriah", "Mathew"), name.givenNames());
        assertEquals("Clarke", name.lastName());
    }

    @Test
    void rejectsNameWithoutGivenNames() {
        InvalidNameException error =
                assertThrows(InvalidNameException.class, () -> new PersonName(List.of(), "Parsons"));

        assertTrue(error.getMessage().contains("1 to 3 given names"));
        assertTrue(error.getMessage().contains("0 were found"));
    }

    @Test
    void rejectsNameWithMoreThanThreeGivenNames() {
        List<String> fourGivenNames = List.of("Hunter", "Uriah", "Mathew", "James");

        InvalidNameException error =
                assertThrows(InvalidNameException.class, () -> new PersonName(fourGivenNames, "Clarke"));

        assertTrue(error.getMessage().contains("4 were found"));
    }

    @Test
    void rejectsBlankLastName() {
        assertThrows(InvalidNameException.class, () -> new PersonName(List.of("Janet"), " "));
    }

    @Test
    void rejectsMissingLastName() {
        assertThrows(InvalidNameException.class, () -> new PersonName(List.of("Janet"), null));
    }

    @Test
    void rejectsMissingGivenNames() {
        assertThrows(InvalidNameException.class, () -> new PersonName(null, "Parsons"));
    }

    @Test
    void rejectsMissingGivenName() {
        List<String> givenNames = new ArrayList<>();
        givenNames.add(null);

        assertThrows(InvalidNameException.class, () -> new PersonName(givenNames, "Parsons"));
    }

    @Test
    void rejectsBlankGivenName() {
        assertThrows(InvalidNameException.class, () -> new PersonName(List.of("Janet", " "), "Parsons"));
    }

    @Test
    void fullNameIsGivenNamesFollowedByLastName() {
        PersonName name = new PersonName(List.of("Adonis", "Julius"), "Archer");

        assertEquals("Adonis Julius", name.givenNamesText());
        assertEquals("Adonis Julius Archer", name.fullName());
    }

    @Test
    void doesNotChangeWhenTheListItWasCreatedFromChanges() {
        List<String> givenNames = new ArrayList<>(List.of("Janet"));
        PersonName name = new PersonName(givenNames, "Parsons");

        givenNames.add("Marie");

        assertEquals("Janet Parsons", name.fullName());
    }
}
