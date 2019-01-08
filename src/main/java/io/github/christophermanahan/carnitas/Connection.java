package io.github.christophermanahan.carnitas;

import java.util.Optional;

public interface Connection extends AutoCloseable {
    Optional<String> receive();

    void send(Response response);

    void close();

    boolean isOpen();
}
