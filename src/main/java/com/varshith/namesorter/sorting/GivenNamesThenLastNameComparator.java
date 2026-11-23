package com.varshith.namesorter.sorting;

import com.varshith.namesorter.model.PersonName;
import java.util.Comparator;
import java.util.List;

public class GivenNamesThenLastNameComparator implements Comparator<PersonName> {

    private static final Comparator<String> MISSING_FIRST_IGNORING_CASE =
            Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER);

    private static final Comparator<PersonName> BY_GIVEN_NAMES_THEN_LAST_NAME =
            Comparator.comparing((PersonName name) -> givenNameAt(name, 0), MISSING_FIRST_IGNORING_CASE)
                    .thenComparing(name -> givenNameAt(name, 1), MISSING_FIRST_IGNORING_CASE)
                    .thenComparing(name -> givenNameAt(name, 2), MISSING_FIRST_IGNORING_CASE)
                    .thenComparing(PersonName::lastName, String.CASE_INSENSITIVE_ORDER);

    @Override
    public int compare(PersonName first, PersonName second) {
        return BY_GIVEN_NAMES_THEN_LAST_NAME.compare(first, second);
    }

    private static String givenNameAt(PersonName name, int position) {
        List<String> givenNames = name.givenNames();
        return position < givenNames.size() ? givenNames.get(position) : null;
    }
}
