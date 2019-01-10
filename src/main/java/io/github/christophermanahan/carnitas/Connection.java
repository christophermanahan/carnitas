package io.github.christophermanahan.carnitas;

public interface Connection extends AutoCloseable {
    Receiver receiver();

    void send(Response response);

    void close();
}
