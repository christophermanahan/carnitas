package io.github.christophermanahan.carnitas;

import java.util.Optional;

public interface Connection extends AutoCloseable, Readable {
    Optional<String> readAll();

    void send(HTTPResponse response);

    void close();
}
