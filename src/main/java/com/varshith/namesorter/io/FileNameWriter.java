package com.varshith.namesorter.io;

import com.varshith.namesorter.model.PersonName;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Writes names to a UTF-8 text file, one per line. An existing file is overwritten. */
public class FileNameWriter implements NameWriter {

    private final Path file;

    public FileNameWriter(Path file) {
        this.file = file;
    }

    /** @throws UncheckedIOException if the file cannot be written */
    @Override
    public void write(List<PersonName> names) {
        List<String> lines = names.stream().map(PersonName::fullName).toList();
        try {
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not write names file: " + file, e);
        }
    }
}
