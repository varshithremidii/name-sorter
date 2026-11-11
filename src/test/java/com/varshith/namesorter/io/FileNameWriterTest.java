package com.varshith.namesorter.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.varshith.namesorter.model.PersonName;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileNameWriterTest {

    private static final PersonName MARIN_ALVAREZ = new PersonName(List.of("Marin"), "Alvarez");
    private static final PersonName ADONIS_JULIUS_ARCHER =
            new PersonName(List.of("Adonis", "Julius"), "Archer");

    @TempDir
    Path directory;

    @Test
    void writesOneNamePerLine() throws IOException {
        Path file = directory.resolve("sorted-names-list.txt");

        new FileNameWriter(file).write(List.of(MARIN_ALVAREZ, ADONIS_JULIUS_ARCHER));

        assertEquals(List.of("Marin Alvarez", "Adonis Julius Archer"), Files.readAllLines(file));
    }

    @Test
    void overwritesAnExistingFile() throws IOException {
        Path file = directory.resolve("sorted-names-list.txt");
        Files.writeString(file, "Old Name\nAnother Old Name\nYet Another Old Name\n");

        new FileNameWriter(file).write(List.of(MARIN_ALVAREZ));

        assertEquals(List.of("Marin Alvarez"), Files.readAllLines(file));
    }

    @Test
    void failsWithAClearMessageWhenTheFileCannotBeWritten() {
        Path notAFile = directory;
        FileNameWriter writer = new FileNameWriter(notAFile);

        UncheckedIOException error =
                assertThrows(UncheckedIOException.class, () -> writer.write(List.of(MARIN_ALVAREZ)));

        assertEquals("Could not write names file: " + notAFile, error.getMessage());
    }
}
