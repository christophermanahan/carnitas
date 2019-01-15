package io.github.christophermanahan.carnitas;

class HTTPRequest2 {
    private final String method;

    HTTPRequest2(String method) {
        this.method = method;
    }

    String method() {
        return method;
    }
}
