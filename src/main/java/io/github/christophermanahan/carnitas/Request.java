package io.github.christophermanahan.carnitas;

import java.util.Optional;

public class Request {
    private final Method method;
    private final String uri;
    private Optional<String> body;

    enum Method {
        GET,
        HEAD,
        POST,
        OPTIONS
    }

    Request(Method method, String uri) {
        this.method = method;
        this.uri = uri;
    }

    private Request(Method method, String uri, Optional<String> body) {
        this.method = method;
        this.uri = uri;
        this.body = body;
    }

    Request withBody(Optional<String> body) {
        return new Request(method, uri, body);
    }

    public Method method() {
        return method;
    }

    public String uri() {
        return uri;
    }

    public Optional<String> body() {
        return body;
    }
}
