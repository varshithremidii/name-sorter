package com.varshith.namesorter.io;

import com.varshith.namesorter.model.PersonName;
import java.util.List;

/** A source of names, for example a text file. */
public interface NameReader {

    List<PersonName> readNames();
}
