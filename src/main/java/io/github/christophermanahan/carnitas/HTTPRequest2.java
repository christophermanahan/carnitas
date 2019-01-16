package io.github.christophermanahan.carnitas;

class HTTPRequest2 {
    private final String method;
    private String body;

    HTTPRequest2(String method) {
        this.method = method;
    }

    private HTTPRequest2(String method, String body) {
        this.method = method;
        this.body = body;
    }

    HTTPRequest2 withBody(String body) {
        return new HTTPRequest2(method, body);
    }

    String method() {
        return method;
    }

    String body() {
        return body;
    }
}
