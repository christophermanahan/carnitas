package io.github.christophermanahan.carnitas;

public interface Logger {
    void log(String message);

    Request log(Request request);

    Response log(Response response);
}
