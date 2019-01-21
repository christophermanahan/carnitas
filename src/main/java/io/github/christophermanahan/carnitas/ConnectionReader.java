package io.github.christophermanahan.carnitas;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class ConnectionReader implements Reader {
    private final Readable connection;

    ConnectionReader(Readable connection) {
        this.connection = connection;
    }

    public Optional<String> readUntil(String delimiter) {
        List<Optional<Character>> read = new ArrayList<>();
        while (notFound(delimiter, read) && successful(read)) {
            read.add(connection.read());
        }
        return successful(read) ? Optional.of(resultUpTo(delimiter, read)) : Optional.empty();
    }

    public Optional<String> read(int numberOfCharacters) {
        List<Optional<Character>> read = readIntoList(numberOfCharacters);
        return successful(read) ? Optional.of(toString(read)): Optional.empty();
    }

    private String toString(List<Optional<Character>> read) {
        return read.stream()
          .flatMap(Optional::stream)
          .map(c -> Character.toString(c))
          .collect(Collectors.joining());
    }

    private boolean successful(List<Optional<Character>> read) {
        return read.stream().noneMatch(Optional::isEmpty);
    }

    private String resultUpTo(String delimiter, List<Optional<Character>> read) {
        return toString(read).substring(0, read.size() - delimiter.length());
    }

    private boolean notFound(String delimiter, List<Optional<Character>> read) {
        return !toString(read).contains(delimiter);
    }

    private List<Optional<Character>> readIntoList(int numberOfCharacters) {
        return IntStream.range(0, numberOfCharacters)
          .mapToObj(i -> connection.read())
          .collect(Collectors.toList());
    }
}
