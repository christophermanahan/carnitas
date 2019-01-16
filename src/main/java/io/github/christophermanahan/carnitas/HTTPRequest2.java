package io.github.christophermanahan.carnitas;

import java.util.Optional;

class HTTPRequest2 {
    private final String method;
    private Optional<String> body = Optional.empty();

    HTTPRequest2(String method) {
        this.method = method;
    }

    HTTPRequest2(String method, Optional<String> body) {
        this.method = method;
        this.body = body;
    }

    HTTPRequest2 withBody(Optional<String> body) {
        return new HTTPRequest2(method, body);
    }

    String method() {
        return method;
    }

    Optional<String> body() {
        return body;
    }
}
