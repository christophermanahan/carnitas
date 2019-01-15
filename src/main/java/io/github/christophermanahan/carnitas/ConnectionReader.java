package io.github.christophermanahan.carnitas;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

class ConnectionReader {
    private final Readable connection;

    ConnectionReader(Readable connection) {
        this.connection = connection;
    }

    String readUntil(String delimiter) {
        StringBuilder read = new StringBuilder();
        while (notFound(delimiter, read)) {
            read.append(connection.read());
        }
        return read.substring(0, read.indexOf(delimiter));
    }

    private boolean notFound(String delimiter, StringBuilder read) {
        return read.indexOf(delimiter) == -1;
    }

    String read(int numberOfCharacters) {
        return IntStream.range(0, numberOfCharacters)
          .mapToObj(i -> readToString())
          .collect(Collectors.joining(""));
    }

    private String readToString() {
        return String.valueOf(connection.read());
    }
}
