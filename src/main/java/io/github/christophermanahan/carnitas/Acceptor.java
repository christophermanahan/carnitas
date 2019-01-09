package io.github.christophermanahan.carnitas;

public interface Acceptor extends AutoCloseable {
    Connection accept();

    boolean isAccepting();

    void close();
}
