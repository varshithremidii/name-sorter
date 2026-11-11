package com.varshith.namesorter.io;

import com.varshith.namesorter.model.InvalidNameException;
import com.varshith.namesorter.model.PersonName;
import com.varshith.namesorter.parsing.NameParser;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Reads names from a UTF-8 text file with one name per line. Blank lines are skipped. */
public class FileNameReader implements NameReader {

    /** An invisible marker some editors, such as Windows Notepad, put at the start of a file. */
    private static final String BYTE_ORDER_MARK = "﻿";

    private final Path file;
    private final NameParser parser;

    public FileNameReader(Path file, NameParser parser) {
        this.file = file;
        this.parser = parser;
    }

    /**
     * @throws InvalidNameException if a line is not a valid name; the message includes the line number
     * @throws UncheckedIOException if the file cannot be read
     */
    @Override
    public List<PersonName> readNames() {
        List<String> lines = readLines();
        List<PersonName> names = new ArrayList<>();

        for (int index = 0; index < lines.size(); index++) {
            String line = lines.get(index);
            if (!line.isBlank()) {
                names.add(parseLine(line, index + 1));
            }
        }
        return names;
    }

    private List<String> readLines() {
        try {
            String content = Files.readString(file, StandardCharsets.UTF_8);
            return withoutByteOrderMark(content).lines().toList();
        } catch (NoSuchFileException e) {
            throw new UncheckedIOException("Names file not found: " + file, e);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read names file: " + file, e);
        }
    }

    private String withoutByteOrderMark(String content) {
        return content.startsWith(BYTE_ORDER_MARK)
                ? content.substring(BYTE_ORDER_MARK.length())
                : content;
    }

    private PersonName parseLine(String line, int lineNumber) {
        try {
            return parser.parse(line);
        } catch (InvalidNameException e) {
            throw new InvalidNameException("Line %d: %s".formatted(lineNumber, e.getMessage()), e);
        }
    }
}
