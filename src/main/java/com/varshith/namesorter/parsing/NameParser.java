package com.varshith.namesorter.parsing;

import com.varshith.namesorter.model.InvalidNameException;
import com.varshith.namesorter.model.PersonName;
import java.util.List;
import java.util.regex.Pattern;

/** Turns one line of text, such as "Hunter Uriah Mathew Clarke", into a {@link PersonName}. */
public class NameParser {

    private static final Pattern WHITESPACE = Pattern.compile("\\s+");

    /**
     * The last word is the last name; every word before it is a given name.
     *
     * @throws InvalidNameException if the text does not follow the rules of a {@link PersonName}
     */
    public PersonName parse(String text) {
        String trimmedText = text.strip();
        List<String> words = List.of(WHITESPACE.split(trimmedText));

        List<String> givenNames = words.subList(0, words.size() - 1);
        String lastName = words.get(words.size() - 1);

        try {
            return new PersonName(givenNames, lastName);
        } catch (InvalidNameException e) {
            throw new InvalidNameException(
                    "\"%s\" is not a valid name: %s".formatted(trimmedText, e.getMessage()), e);
        }
    }
}
