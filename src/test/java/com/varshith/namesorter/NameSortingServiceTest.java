package com.varshith.namesorter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.varshith.namesorter.io.NameReader;
import com.varshith.namesorter.io.NameWriter;
import com.varshith.namesorter.model.InvalidNameException;
import com.varshith.namesorter.model.PersonName;
import com.varshith.namesorter.sorting.LastNameThenGivenNamesComparator;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Uses in-memory readers and writers, so these tests never touch the disk or the screen. */
class NameSortingServiceTest {

    private static final PersonName JANET_PARSONS = new PersonName(List.of("Janet"), "Parsons");
    private static final PersonName MARIN_ALVAREZ = new PersonName(List.of("Marin"), "Alvarez");
    private static final PersonName LEO_GARDNER = new PersonName(List.of("Leo"), "Gardner");

    private final NameReader reader = () -> List.of(JANET_PARSONS, MARIN_ALVAREZ, LEO_GARDNER);
    private final RecordingNameWriter screen = new RecordingNameWriter();
    private final RecordingNameWriter file = new RecordingNameWriter();

    @Test
    void givesTheSortedNamesToEveryWriter() {
        NameSortingService service = new NameSortingService(
                reader, new LastNameThenGivenNamesComparator(), List.of(screen, file));

        service.sortNames();

        List<PersonName> sortedNames = List.of(MARIN_ALVAREZ, LEO_GARDNER, JANET_PARSONS);
        assertEquals(sortedNames, screen.writtenNames);
        assertEquals(sortedNames, file.writtenNames);
    }

    @Test
    void sortsInWhateverOrderItIsGiven() {
        Comparator<PersonName> reverseOrder = new LastNameThenGivenNamesComparator().reversed();
        NameSortingService service = new NameSortingService(reader, reverseOrder, List.of(screen));

        service.sortNames();

        assertEquals(List.of(JANET_PARSONS, LEO_GARDNER, MARIN_ALVAREZ), screen.writtenNames);
    }

    @Test
    void writesNothingWhenTheNamesCannotBeRead() {
        NameReader failingReader = () -> {
            throw new InvalidNameException("Line 1: not a valid name");
        };
        NameSortingService service = new NameSortingService(
                failingReader, new LastNameThenGivenNamesComparator(), List.of(screen, file));

        assertThrows(InvalidNameException.class, service::sortNames);

        assertFalse(screen.wasCalled());
        assertFalse(file.wasCalled());
    }

    private static class RecordingNameWriter implements NameWriter {

        private List<PersonName> writtenNames;

        @Override
        public void write(List<PersonName> names) {
            writtenNames = names;
        }

        boolean wasCalled() {
            return writtenNames != null;
        }
    }
}
