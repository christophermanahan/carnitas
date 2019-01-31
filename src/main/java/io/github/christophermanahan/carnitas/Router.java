package io.github.christophermanahan.carnitas;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class Router implements Handler {
    private final HashMap<Route, Function<HTTPRequest, HTTPResponse>> map;

    Router() {
        this.map = new HashMap<>();
    }

    Router get(String uri, Function<HTTPRequest, HTTPResponse> handler) {
        map.put(new Route(HTTPRequest.Method.GET, uri), handler);
        return this;
    }

    Router head(String uri, Function<HTTPRequest, HTTPResponse> handler) {
        map.put(new Route(HTTPRequest.Method.HEAD, uri), handler);
        return this;
    }

    Router post(String uri, Function<HTTPRequest, HTTPResponse> handler) {
        map.put(new Route(HTTPRequest.Method.POST, uri), handler);
        return this;
    }

    public HTTPResponse handle(HTTPRequest request) {
        return map.keySet().stream()
          .filter(matches(request))
          .findFirst()
          .map(map::get)
          .map(handler -> handler.apply(request))
          .orElseGet(handler(request));
    }

    private Predicate<Route> matches(HTTPRequest request) {
        return route -> route.equals(new Route(request.method(), request.uri()));
    }

    private Supplier<HTTPResponse> handler(HTTPRequest request) {
        List<HTTPRequest.Method> allowed = allowed(request);
        return allowed.isEmpty() ? notFound() : not(allowed);
    }

    private List<HTTPRequest.Method> allowed(HTTPRequest request) {
        return map.keySet().stream()
          .filter(matchesUriOf(request))
          .map(Route::method)
          .collect(Collectors.toList());
    }

    private Predicate<Route> matchesUriOf(HTTPRequest request) {
        return route -> route.uri().equals(request.uri());
    }

    private Supplier<HTTPResponse> not(List<HTTPRequest.Method> allowed) {
        return new ResponseBuilder()
          .set(HTTPResponse.Status.METHOD_NOT_ALLOWED)
          .set(new Headers()
            .contentLength(0)
            .allow(allowed)
          );
    }

    private Supplier<HTTPResponse> notFound() {
        return new ResponseBuilder()
          .set(HTTPResponse.Status.NOT_FOUND)
          .set(new Headers()
            .contentLength(0)
          );
    }
}
