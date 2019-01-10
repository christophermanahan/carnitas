package io.github.christophermanahan.carnitas;

public interface RequestBuilder {
    public RequestBuilder requestMethod(String requestMethod);
    public RequestBuilder body(String body);
    public Request build();
}
