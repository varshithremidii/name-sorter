package com.varshith.namesorter.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.varshith.namesorter.model.InvalidNameException;
import com.varshith.namesorter.model.PersonName;
import com.varshith.namesorter.parsing.NameParser;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileNameReaderTest {

    @TempDir
    Path directory;

    @Test
    void readsOneNamePerLineInFileOrder() throws IOException {
        Path file = fileContaining("Janet Parsons\nMarin Alvarez\n");

        assertEquals(List.of("Janet Parsons", "Marin Alvarez"), fullNamesIn(file));
    }

    @Test
    void skipsBlankLines() throws IOException {
        Path file = fileContaining("\nJanet Parsons\n   \nMarin Alvarez\n\n");

        assertEquals(List.of("Janet Parsons", "Marin Alvarez"), fullNamesIn(file));
    }

    @Test
    void readsFilesWithWindowsLineEndings() throws IOException {
        Path file = fileContaining("Janet Parsons\r\nMarin Alvarez\r\n");

        assertEquals(List.of("Janet Parsons", "Marin Alvarez"), fullNamesIn(file));
    }

    @Test
    void ignoresTheByteOrderMarkSomeEditorsAddToTheStartOfAFile() throws IOException {
        Path file = fileContaining("﻿Janet Parsons\nMarin Alvarez\n");

        assertEquals(List.of("Janet Parsons", "Marin Alvarez"), fullNamesIn(file));
    }

    @Test
    void returnsNoNamesForAnEmptyFile() throws IOException {
        Path file = fileContaining("");

        assertEquals(List.of(), fullNamesIn(file));
    }

    @Test
    void saysWhichLineHoldsAnInvalidName() throws IOException {
        Path file = fileContaining("Janet Parsons\n\nMadonna\n");
        FileNameReader reader = new FileNameReader(file, new NameParser());

        InvalidNameException error = assertThrows(InvalidNameException.class, reader::readNames);

        assertTrue(error.getMessage().startsWith("Line 3: \"Madonna\" is not a valid name"));
    }

    @Test
    void failsWithAClearMessageWhenTheFileDoesNotExist() {
        Path missingFile = directory.resolve("no-such-file.txt");
        FileNameReader reader = new FileNameReader(missingFile, new NameParser());

        UncheckedIOException error = assertThrows(UncheckedIOException.class, reader::readNames);

        assertEquals("Names file not found: " + missingFile, error.getMessage());
    }

    private Path fileContaining(String content) throws IOException {
        return Files.writeString(directory.resolve("names.txt"), content, StandardCharsets.UTF_8);
    }

    private List<String> fullNamesIn(Path file) {
        return new FileNameReader(file, new NameParser()).readNames().stream()
                .map(PersonName::fullName)
                .toList();
    }
}
