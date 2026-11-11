# Name Sorter

Sorts a list of names by last name, then by given names. The sorted list is printed to the
screen and written to `sorted-names-list.txt` in the working directory.

## Requirements

- JDK 17 or newer
- Maven 3.9 or newer

## Build

```
mvn package
```

This runs the tests and creates `target/name-sorter.jar`.

## Run

```
./name-sorter ./unsorted-names-list.txt      (macOS, Linux, Git Bash)
name-sorter .\unsorted-names-list.txt        (Windows)
```

Both scripts are thin wrappers around `java -jar target/name-sorter.jar <file>`.
A sample `unsorted-names-list.txt` is included in the repository.

The program exits with code `0` on success. If something is wrong it prints the reason and exits
with code `1`, for example:

```
Error: Line 14: "Madonna" is not a valid name: a name must have 1 to 3 given names before the last name, but 0 were found
```

## Test

```
mvn test
```

## How the code is organised

| Class | Responsibility |
| --- | --- |
| `NameSorterApp` | Entry point. Reads the argument and wires the pieces together. |
| `NameSortingService` | The flow: read the names, sort them, give them to every writer. |
| `model.PersonName` | One name. Enforces "1 to 3 given names and a last name". |
| `parsing.NameParser` | Turns a line of text into a `PersonName`. |
| `sorting.LastNameThenGivenNamesComparator` | The sort order. |
| `io.NameReader` / `FileNameReader` | Where names come from. |
| `io.NameWriter` / `ConsoleNameWriter`, `FileNameWriter` | Where sorted names go. |

`NameSortingService` depends only on `NameReader`, `Comparator<PersonName>` and `NameWriter`.
Adding another destination (say, a CSV file) means adding one `NameWriter`; nothing else changes.
It also means the service is tested with in-memory readers and writers.

## Assumptions

The brief leaves a few things open. These are the choices made:

- **The last word on a line is the last name.** Everything before it is a given name.
- **Sorting ignores upper and lower case**, so `mcdonald` and `McDonald` sort together. Names are
  printed with the capitalisation they were given.
- **Blank lines are skipped.** Extra spaces around or between words are ignored.
- **An invalid name stops the program** and reports the line number, rather than being dropped
  silently. No output is produced, so a partial list is never mistaken for a complete one.
- **Files are UTF-8.** A byte order mark at the start of the file is ignored.
- The sample output in the brief shows `Vaughn Lewis` where the sample input has `Vaugh Lewis`.
  This is treated as a typo in the brief; names are never altered.

## Continuous integration

GitHub Actions builds the project, runs the tests and runs the program against the sample list
on every push and pull request. See `.github/workflows/build.yml`.
