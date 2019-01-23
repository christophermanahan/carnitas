package io.github.christophermanahan.carnitas;

import java.util.Optional;

public class HTTPRequest2 {
    private final String method;
    private final String uri;
    private Optional<String> body;

    HTTPRequest2(String method, String uri) {
        this.method = method;
        this.uri = uri;
    }

    private HTTPRequest2(String method, String uri, Optional<String> body) {
        this.method = method;
        this.uri = uri;
        this.body = body;
    }

    public HTTPRequest2 withBody(Optional<String> body) {
        return new HTTPRequest2(method, uri, body);
    }

    public String method() {
        return method;
    }

    public String uri() {
        return uri;
    }

    public Optional<String> body() {
        return body;
    }
}
