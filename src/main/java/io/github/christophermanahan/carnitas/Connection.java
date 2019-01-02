package io.github.christophermanahan.carnitas;

import java.util.Optional;

public interface Connection {
    Optional<String> receive();

    void send(Response response);

    void close();

    boolean isOpen();
}
