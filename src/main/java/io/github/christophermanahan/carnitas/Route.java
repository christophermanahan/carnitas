package io.github.christophermanahan.carnitas;

import java.util.function.Function;

public class Route {
    private final String uri;
    private final Function<HTTPRequest2, HTTPResponse> handler;

    public Route(String uri, Function<HTTPRequest2, HTTPResponse> handler) {
        this.uri = uri;
        this.handler = handler;
    }

    public String uri() {
        return uri;
    }

    public Function<HTTPRequest2, HTTPResponse> handler() {
        return handler;
    }
}
