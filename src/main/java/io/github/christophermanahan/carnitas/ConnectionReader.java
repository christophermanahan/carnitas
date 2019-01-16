package io.github.christophermanahan.carnitas;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class ConnectionReader implements Reader {
    private final Readable connection;

    ConnectionReader(Readable connection) {
        this.connection = connection;
    }

    public Optional<String> readUntil(String delimiter) {
        StringBuilder read = new StringBuilder();
        while (notFound(delimiter, read)) {
            read.append(connection.read());
        }
        return Optional.of(
          read.substring(0, read.indexOf(delimiter))
        );
    }

    private boolean notFound(String delimiter, StringBuilder read) {
        return read.indexOf(delimiter) == -1;
    }

    public Optional<String> read(int numberOfCharacters) {
        return Optional.of(
          IntStream.range(0, numberOfCharacters)
            .mapToObj(i -> readToString())
            .collect(Collectors.joining(""))
        );
    }

    private String readToString() {
        return String.valueOf(connection.read());
    }
}
