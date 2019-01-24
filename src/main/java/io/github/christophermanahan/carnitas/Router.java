package io.github.christophermanahan.carnitas;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

class Router implements Handler {
    private final HashMap<Route, Function<HTTPRequest, HTTPResponse>> map;
    static final String GET = "GET";
    static final String HEAD = "HEAD";
    static final String POST = "POST";

    Router() {
        this.map = new HashMap<>();
    }

    Router get(String uri, Function<HTTPRequest, HTTPResponse> handler) {
        map.put(new Route(GET, uri), handler);
        return this;
    }

    Router head(String uri, Function<HTTPRequest, HTTPResponse> handler) {
        map.put(new Route(HEAD, uri), handler);
        return this;
    }

    Router post(String uri, Function<HTTPRequest, HTTPResponse> handler) {
        map.put(new Route(POST, uri), handler);
        return this;
    }

    public HTTPResponse handle(HTTPRequest request) {
        if (routeAdded(request)) {
            return handleRoute(request);
        } else if (uriAddedToAnyMethod(request)) {
            return methodNotAllowed(request);
        } else {
            return notFound();
        }
    }

    private Route route(HTTPRequest request) {
        return new Route(request.method(), request.uri());
    }

    private boolean routeAdded(HTTPRequest request) {
        return map.keySet().stream()
          .anyMatch(route -> route.equals(route(request)));
    }

    private HTTPResponse handleRoute(HTTPRequest request) {
        return map.get(
          map.keySet().stream()
            .filter(route -> route.equals(route(request)))
            .findFirst()
            .get()
        ).apply(request);
    }

    private boolean uriAddedToAnyMethod(HTTPRequest request) {
        return map.keySet().stream()
          .anyMatch(route -> route.uri().equals(request.uri()));
    }

    private HTTPResponse methodNotAllowed(HTTPRequest request) {
        return new HTTPResponse(HTTPResponse.Status.METHOD_NOT_ALLOWED)
          .withHeaders(List.of(
            Headers.allow(allowedMethods(request)))
          );
    }

    private List<String> allowedMethods(HTTPRequest request) {
        return map.keySet().stream()
          .filter(route -> route.uri().equals(request.uri()))
          .map(Route::method)
          .collect(Collectors.toList());
    }

    private HTTPResponse notFound() {
        return new HTTPResponse(HTTPResponse.Status.NOT_FOUND);
    }
}
