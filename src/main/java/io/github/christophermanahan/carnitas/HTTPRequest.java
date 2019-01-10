package io.github.christophermanahan.carnitas;

public class HTTPRequest implements Request {
    private final RequestMethod requestMethod;
    private final String body;

    HTTPRequest(RequestMethod requestMethod, String body) {
        this.requestMethod = requestMethod;
        this.body = body;
    }

    public RequestMethod requestMethod() {
        return requestMethod;
    }

    public String body() {
        return body;
    }
}
