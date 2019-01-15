package io.github.christophermanahan.carnitas;

public interface RunContext {
    void accept(Runnable runnable);
}
