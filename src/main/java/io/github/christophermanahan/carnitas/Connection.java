package io.github.christophermanahan.carnitas;

public interface Connection extends AutoCloseable, Readable {
    void send(HTTPResponse response);

    void close();
}
