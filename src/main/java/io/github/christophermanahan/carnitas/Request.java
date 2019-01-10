package io.github.christophermanahan.carnitas;

public interface Request {
    public RequestMethod requestMethod();
    public String body();
}
