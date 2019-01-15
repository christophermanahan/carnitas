package io.github.christophermanahan.carnitas;

public interface Connection extends AutoCloseable, Readable {
    Receiver receiver();

    void send(Response response);

    void close();
}
