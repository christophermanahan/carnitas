package io.github.christophermanahan.carnitas;

import java.util.Optional;

class HTTPRequest {
    private final String method;
    private Optional<String> body = Optional.empty();

    HTTPRequest(String method) {
        this.method = method;
    }

    HTTPRequest(String method, Optional<String> body) {
        this.method = method;
        this.body = body;
    }

    HTTPRequest withBody(Optional<String> body) {
        return new HTTPRequest(method, body);
    }

    String method() {
        return method;
    }

    Optional<String> body() {
        return body;
    }
}
