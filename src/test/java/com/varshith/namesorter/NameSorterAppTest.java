package com.varshith.namesorter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Runs the whole program, from the command-line argument to the screen and the output file. */
class NameSorterAppTest {

    private static final List<String> UNSORTED_NAMES_FROM_THE_BRIEF = List.of(
            "Janet Parsons",
            "Vaughn Lewis",
            "Adonis Julius Archer",
            "Shelby Nathan Yoder",
            "Marin Alvarez",
            "London Lindsey",
            "Beau Tristan Bentley",
            "Leo Gardner",
            "Hunter Uriah Mathew Clarke",
            "Mikayla Lopez",
            "Frankie Conner Ritter");

    private static final List<String> SORTED_NAMES_FROM_THE_BRIEF = List.of(
            "Marin Alvarez",
            "Adonis Julius Archer",
            "Beau Tristan Bentley",
            "Hunter Uriah Mathew Clarke",
            "Leo Gardner",
            "Vaughn Lewis",
            "London Lindsey",
            "Mikayla Lopez",
            "Janet Parsons",
            "Frankie Conner Ritter",
            "Shelby Nathan Yoder");

    @TempDir
    Path workingDirectory;

    private final ByteArrayOutputStream screen = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errors = new ByteArrayOutputStream();

    @Test
    void printsTheSortedNamesToTheScreen() throws IOException {
        Path unsortedNamesFile = fileContaining(UNSORTED_NAMES_FROM_THE_BRIEF);

        int exitCode = run(unsortedNamesFile.toString());

        assertEquals(0, exitCode);
        assertEquals(SORTED_NAMES_FROM_THE_BRIEF, linesOf(screen));
    }

    @Test
    void writesTheSortedNamesToAFileInTheWorkingDirectory() throws IOException {
        Path unsortedNamesFile = fileContaining(UNSORTED_NAMES_FROM_THE_BRIEF);

        run(unsortedNamesFile.toString());

        assertEquals(SORTED_NAMES_FROM_THE_BRIEF, Files.readAllLines(sortedNamesFile()));
    }

    @Test
    void explainsHowToRunItWhenNoFileIsGiven() {
        int exitCode = run();

        assertEquals(1, exitCode);
        assertEquals(List.of("Usage: name-sorter <path-to-names-file>"), linesOf(errors));
    }

    @Test
    void reportsAMissingFile() {
        Path missingFile = workingDirectory.resolve("no-such-file.txt");

        int exitCode = run(missingFile.toString());

        assertEquals(1, exitCode);
        assertEquals(List.of("Error: Names file not found: " + missingFile), linesOf(errors));
    }

    @Test
    void reportsAnInvalidNameAndProducesNoOutput() throws IOException {
        Path unsortedNamesFile = fileContaining(List.of("Janet Parsons", "Madonna"));

        int exitCode = run(unsortedNamesFile.toString());

        assertEquals(1, exitCode);
        assertTrue(linesOf(errors).get(0).startsWith("Error: Line 2: \"Madonna\" is not a valid name"));
        assertEquals(List.of(), linesOf(screen));
        assertFalse(Files.exists(sortedNamesFile()));
    }

    private int run(String... args) {
        return NameSorterApp.run(args, printStreamTo(screen), printStreamTo(errors), workingDirectory);
    }

    private Path fileContaining(List<String> lines) throws IOException {
        return Files.write(workingDirectory.resolve("unsorted-names-list.txt"), lines);
    }

    private Path sortedNamesFile() {
        return workingDirectory.resolve(NameSorterApp.SORTED_NAMES_FILE_NAME);
    }

    private static PrintStream printStreamTo(ByteArrayOutputStream output) {
        return new PrintStream(output, true, StandardCharsets.UTF_8);
    }

    private static List<String> linesOf(ByteArrayOutputStream output) {
        return output.toString(StandardCharsets.UTF_8).lines().toList();
    }
}
