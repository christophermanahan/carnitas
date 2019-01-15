package io.github.christophermanahan.carnitas;

public interface Listener extends AutoCloseable {
    Connection listen();

    boolean isListening();

    void close();
}
