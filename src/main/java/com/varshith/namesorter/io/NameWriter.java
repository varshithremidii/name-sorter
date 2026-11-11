package com.varshith.namesorter.io;

import com.varshith.namesorter.model.PersonName;
import java.util.List;

/** A destination for names, for example the screen or a text file. */
public interface NameWriter {

    void write(List<PersonName> names);
}
