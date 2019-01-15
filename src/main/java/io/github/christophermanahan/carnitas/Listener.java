package io.github.christophermanahan.carnitas;

public interface Listener extends AutoCloseable {
    SocketConnection listen();

    boolean isListening();

    void close();
}
