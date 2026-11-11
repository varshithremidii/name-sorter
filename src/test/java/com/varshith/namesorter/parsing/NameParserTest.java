package com.varshith.namesorter.parsing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.varshith.namesorter.model.InvalidNameException;
import com.varshith.namesorter.model.PersonName;
import java.util.List;
import org.junit.jupiter.api.Test;

class NameParserTest {

    private final NameParser parser = new NameParser();

    @Test
    void treatsTheLastWordAsTheLastName() {
        PersonName name = parser.parse("Janet Parsons");

        assertEquals(new PersonName(List.of("Janet"), "Parsons"), name);
    }

    @Test
    void treatsEveryWordBeforeTheLastAsAGivenName() {
        PersonName name = parser.parse("Hunter Uriah Mathew Clarke");

        assertEquals(new PersonName(List.of("Hunter", "Uriah", "Mathew"), "Clarke"), name);
    }

    @Test
    void ignoresExtraSpacesAndTabs() {
        PersonName name = parser.parse("  Adonis \t Julius   Archer ");

        assertEquals(new PersonName(List.of("Adonis", "Julius"), "Archer"), name);
    }

    @Test
    void rejectsASingleWordBecauseItHasNoGivenName() {
        InvalidNameException error =
                assertThrows(InvalidNameException.class, () -> parser.parse("Madonna"));

        assertTrue(error.getMessage().startsWith("\"Madonna\" is not a valid name"));
    }

    @Test
    void rejectsMoreThanThreeGivenNames() {
        assertThrows(
                InvalidNameException.class, () -> parser.parse("Hunter Uriah Mathew James Clarke"));
    }

    @Test
    void rejectsBlankText() {
        assertThrows(InvalidNameException.class, () -> parser.parse("   "));
    }
}
