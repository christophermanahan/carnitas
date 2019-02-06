package io.github.christophermanahan.carnitas;

public class MessageLogger implements Logger {
    public void log(String message) {
        System.out.println(message);
    }
}
