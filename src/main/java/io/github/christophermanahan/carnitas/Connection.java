package io.github.christophermanahan.carnitas;

public interface Connection extends AutoCloseable, Readable {
    void send(HTTPResponse response);

    void send2(HTTPResponse2 response2);

    void close();
}
