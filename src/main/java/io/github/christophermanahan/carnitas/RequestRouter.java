package io.github.christophermanahan.carnitas;

import java.util.HashMap;
import java.util.function.Function;

class RequestRouter implements Handler {
    private final HashMap<Route, Function<HTTPRequest, HTTPResponse>> map;
    static final String GET = "GET";
    static final String HEAD = "HEAD";
    static final String POST = "POST";

    RequestRouter() {
        this.map = new HashMap<>();
    }

    RequestRouter get(String uri, Function<HTTPRequest, HTTPResponse> handler) {
        map.put(new Route(GET, uri), handler);
        return this;
    }

    RequestRouter head(String uri, Function<HTTPRequest, HTTPResponse> handler) {
        map.put(new Route(HEAD, uri), handler);
        return this;
    }

    RequestRouter post(String uri, Function<HTTPRequest, HTTPResponse> handler) {
        map.put(new Route(POST, uri), handler);
        return this;
    }

    public HTTPResponse handle(HTTPRequest request) {
        if (routeAdded(request)) {
            return handleRoute(request);
        } else {
            return new HTTPResponse(HTTPResponse.StatusCode.NOT_FOUND);
        }
    }

    private HTTPResponse handleRoute(HTTPRequest request) {
        return map.get(
          map.keySet().stream()
            .filter(route -> route.equals(route(request)))
            .findFirst()
            .get()
        ).apply(request);
    }

    private boolean routeAdded(HTTPRequest request) {
        return map.keySet().stream()
          .anyMatch(route -> route.equals(route(request)));
    }

    private Route route(HTTPRequest request) {
        return new Route(request.method(), request.uri());
    }
}
