package io.github.christophermanahan.carnitas;

import java.util.function.Function;

public class Route {
    private final String uri;
    private final Function<HTTPRequest, HTTPResponse> handler;

    public Route(String uri, Function<HTTPRequest, HTTPResponse> handler) {
        this.uri = uri;
        this.handler = handler;
    }

    public String uri() {
        return uri;
    }

    public Function<HTTPRequest, HTTPResponse> handler() {
        return handler;
    }
}
