package com.varshith.namesorter.model;

import java.util.List;
import java.util.Objects;

/**
 * A person's name: one to three given names followed by a last name.
 *
 * <p>The rule is enforced here, so an invalid name cannot exist anywhere else in the program.
 */
public record PersonName(List<String> givenNames, String lastName) {

    public static final int MIN_GIVEN_NAMES = 1;
    public static final int MAX_GIVEN_NAMES = 3;

    public PersonName {
        if (givenNames == null || givenNames.stream().anyMatch(Objects::isNull)) {
            throw new InvalidNameException("a given name cannot be missing");
        }
        givenNames = List.copyOf(givenNames);

        if (lastName == null || lastName.isBlank()) {
            throw new InvalidNameException("a name must have a last name");
        }
        if (givenNames.size() < MIN_GIVEN_NAMES || givenNames.size() > MAX_GIVEN_NAMES) {
            throw new InvalidNameException(
                    "a name must have %d to %d given names before the last name, but %d were found"
                            .formatted(MIN_GIVEN_NAMES, MAX_GIVEN_NAMES, givenNames.size()));
        }
        if (givenNames.stream().anyMatch(String::isBlank)) {
            throw new InvalidNameException("a given name cannot be blank");
        }
    }

    /** All given names as one piece of text, for example "Hunter Uriah Mathew". */
    public String givenNamesText() {
        return String.join(" ", givenNames);
    }

    /** The name as it is written: given names first, last name last. */
    public String fullName() {
        return givenNamesText() + " " + lastName;
    }
}
