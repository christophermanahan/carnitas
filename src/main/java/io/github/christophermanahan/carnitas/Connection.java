package io.github.christophermanahan.carnitas;

public interface Connection {
    Receiver receiver();

    void send(Response response);

    void close();

    boolean isOpen();
}
