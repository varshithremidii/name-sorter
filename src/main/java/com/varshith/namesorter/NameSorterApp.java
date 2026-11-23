package com.varshith.namesorter;

import com.varshith.namesorter.io.ConsoleNameWriter;
import com.varshith.namesorter.io.FileNameReader;
import com.varshith.namesorter.io.FileNameWriter;
import com.varshith.namesorter.model.InvalidNameException;
import com.varshith.namesorter.model.PersonName;
import com.varshith.namesorter.parsing.NameParser;
import com.varshith.namesorter.sorting.GivenNamesThenLastNameComparator;
import com.varshith.namesorter.sorting.LastNameThenGivenNamesComparator;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

/**
 * Command-line entry point: {@code name-sorter <path-to-names-file>}.
 *
 * <p>This is the only place that decides which concrete reader, sort order and writers are used.
 */
public final class NameSorterApp {

    static final String SORTED_NAMES_FILE_NAME = "sorted-names-list.txt";

    private static final int EXIT_SUCCESS = 0;
    private static final int EXIT_FAILURE = 1;

    private static final String SORT_BY_LAST_NAME = "--sort=last";
    private static final String SORT_BY_GIVEN_NAMES = "--sort=given";

    private NameSorterApp() {
    }

    public static void main(String[] args) {
        Path workingDirectory = Path.of("");
        System.exit(run(args, System.out, System.err, workingDirectory));
    }

    /**
     * Sorts the names in the file given as the only argument.
     *
     * @return the process exit code: 0 when the names were sorted, 1 when they could not be
     */
    static int run(String[] args, PrintStream screen, PrintStream errors, Path outputDirectory) {
        if (args.length < 1 || args.length > 2) {
            errors.println("Usage: name-sorter <path-to-names-file>");
            return EXIT_FAILURE;
        }

        String sortOption = args.length == 2 ? args[0] : SORT_BY_LAST_NAME;
        Comparator<PersonName> sortOrder;
        if (SORT_BY_LAST_NAME.equals(sortOption)) {
            sortOrder = new LastNameThenGivenNamesComparator();
        } else if (SORT_BY_GIVEN_NAMES.equals(sortOption)) {
            sortOrder = new GivenNamesThenLastNameComparator();
        } else {
            errors.println("Error: Unknown sort option: " + sortOption
                    + " (use " + SORT_BY_LAST_NAME + " or " + SORT_BY_GIVEN_NAMES + ")");
            return EXIT_FAILURE;
        }

        Path unsortedNamesFile = Path.of(args[args.length - 1]);
        Path sortedNamesFile = outputDirectory.resolve(SORTED_NAMES_FILE_NAME);

        NameSortingService service = new NameSortingService(
                new FileNameReader(unsortedNamesFile, new NameParser()),
                sortOrder,
                List.of(new ConsoleNameWriter(screen), new FileNameWriter(sortedNamesFile)));

        try {
            service.sortNames();
            return EXIT_SUCCESS;
        } catch (InvalidNameException | UncheckedIOException e) {
            errors.println("Error: " + e.getMessage());
            return EXIT_FAILURE;
        }
    }
}
