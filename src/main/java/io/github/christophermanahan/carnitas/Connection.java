package io.github.christophermanahan.carnitas;

public interface Connection extends AutoCloseable, Readable {
    void send(Response response);

    void close();
}
