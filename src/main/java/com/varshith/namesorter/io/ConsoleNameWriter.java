package com.varshith.namesorter.io;

import com.varshith.namesorter.model.PersonName;
import java.io.PrintStream;
import java.util.List;

/** Prints names to the screen, one per line. */
public class ConsoleNameWriter implements NameWriter {

    private final PrintStream console;

    public ConsoleNameWriter(PrintStream console) {
        this.console = console;
    }

    @Override
    public void write(List<PersonName> names) {
        names.forEach(name -> console.println(name.fullName()));
    }
}
