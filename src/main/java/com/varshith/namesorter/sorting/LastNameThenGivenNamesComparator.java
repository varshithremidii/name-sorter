package com.varshith.namesorter.sorting;

import com.varshith.namesorter.model.PersonName;
import java.util.Comparator;

/**
 * The order the names are sorted in: by last name first, then by given names when last names match.
 *
 * <p>Upper and lower case are treated as equal, so "mcdonald" and "McDonald" sort together.
 */
public class LastNameThenGivenNamesComparator implements Comparator<PersonName> {

    private static final Comparator<PersonName> BY_LAST_NAME_THEN_GIVEN_NAMES =
            Comparator.comparing(PersonName::lastName, String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(PersonName::givenNamesText, String.CASE_INSENSITIVE_ORDER);

    @Override
    public int compare(PersonName first, PersonName second) {
        return BY_LAST_NAME_THEN_GIVEN_NAMES.compare(first, second);
    }
}
