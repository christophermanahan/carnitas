package io.github.christophermanahan.carnitas;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class RequestBuilder {
    private HTTPRequest.Method method;
    private String uri;
    private List<String> headers = new ArrayList<>();
    private Optional<String> body = Optional.empty();

    RequestBuilder setMethod(HTTPRequest.Method method) {
        this.method = method;
        return this;
    }

    RequestBuilder setUri(String uri) {
        this.uri = uri;
        return this;
    }

    RequestBuilder addHeader(String header) {
        this.headers.add(header);
        return this;
    }

    RequestBuilder setBody(Optional<String> body) {
        this.body = body;
        return this;
    }

    public HTTPRequest get() {
        return new HTTPRequest(new Route(method, uri), headers, body);
    }
}
