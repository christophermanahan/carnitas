package io.github.christophermanahan.carnitas;

public class ErrorLogger implements Logger {

    public void log(String message) {
        System.out.println(message);
    }
}
