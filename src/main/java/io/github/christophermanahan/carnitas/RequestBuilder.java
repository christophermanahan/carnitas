package io.github.christophermanahan.carnitas;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class RequestBuilder {
    private Route route;
    private List<String> headers = new ArrayList<>();
    private Optional<String> body = Optional.empty();

    RequestBuilder setRoute(Route route) {
        this.route = route;
        return this;
    }

    RequestBuilder addHeader(String header) {
        this.headers.add(header);
        return this;
    }

    public RequestBuilder setBody(Optional<String> body) {
        this.body = body;
        return this;
    }

    public HTTPRequest get() {
        return new HTTPRequest(route, headers, body);
    }
}
