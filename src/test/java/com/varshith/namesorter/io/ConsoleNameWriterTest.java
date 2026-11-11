package com.varshith.namesorter.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.varshith.namesorter.model.PersonName;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

class ConsoleNameWriterTest {

    @Test
    void printsOneNamePerLine() {
        ByteArrayOutputStream printed = new ByteArrayOutputStream();
        PrintStream console = new PrintStream(printed, true, StandardCharsets.UTF_8);
        List<PersonName> names = List.of(
                new PersonName(List.of("Marin"), "Alvarez"),
                new PersonName(List.of("Adonis", "Julius"), "Archer"));

        new ConsoleNameWriter(console).write(names);

        assertEquals(
                List.of("Marin Alvarez", "Adonis Julius Archer"),
                printed.toString(StandardCharsets.UTF_8).lines().toList());
    }
}
