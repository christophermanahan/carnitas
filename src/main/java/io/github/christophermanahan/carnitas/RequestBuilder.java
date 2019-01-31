package io.github.christophermanahan.carnitas;

import java.util.Optional;

class RequestBuilder {
    private HTTPRequest.Method method;
    private String uri;
    private Headers headers = new Headers();
    private Optional<String> body = Optional.empty();

    RequestBuilder set(HTTPRequest.Method method) {
        this.method = method;
        return this;
    }

    RequestBuilder set(String uri) {
        this.uri = uri;
        return this;
    }

    RequestBuilder set(Headers headers) {
        this.headers = headers;
        return this;
    }

    RequestBuilder set(Optional<String> body) {
        this.body = body;
        return this;
    }

    public HTTPRequest get() {
        return new HTTPRequest(new Route(method, uri), headers, body);
    }
}
