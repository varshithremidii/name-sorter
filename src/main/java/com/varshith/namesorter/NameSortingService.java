package com.varshith.namesorter;

import com.varshith.namesorter.io.NameReader;
import com.varshith.namesorter.io.NameWriter;
import com.varshith.namesorter.model.PersonName;
import java.util.Comparator;
import java.util.List;

/**
 * The whole job in one place: read the names, sort them, write them to every destination.
 *
 * <p>It only knows the abstractions. Where names come from, the order they are sorted in and
 * where they go can each change without this class changing.
 */
public class NameSortingService {

    private final NameReader reader;
    private final Comparator<PersonName> sortOrder;
    private final List<NameWriter> writers;

    public NameSortingService(
            NameReader reader, Comparator<PersonName> sortOrder, List<NameWriter> writers) {
        this.reader = reader;
        this.sortOrder = sortOrder;
        this.writers = List.copyOf(writers);
    }

    public void sortNames() {
        List<PersonName> sortedNames = reader.readNames().stream().sorted(sortOrder).toList();
        writers.forEach(writer -> writer.write(sortedNames));
    }
}
