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
        mapRoute(HTTPRequest.Method.GET, uri, handler);
        return this;
    }

    Router head(String uri, Function<HTTPRequest, HTTPResponse> handler) {
        mapRoute(HTTPRequest.Method.HEAD, uri, handler);
        return this;
    }

    Router post(String uri, Function<HTTPRequest, HTTPResponse> handler) {
        mapRoute(HTTPRequest.Method.POST, uri, handler);
        return this;
    }

    private void mapRoute(HTTPRequest.Method method, String uri, Function<HTTPRequest, HTTPResponse> handler) {
        map.put(new Route(method, uri), handler);
        map.putIfAbsent(new Route(HTTPRequest.Method.OPTIONS, uri), options());
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
          .filter(route -> route.uri().equals(request.uri()))
          .map(Route::method)
          .collect(Collectors.toList());
    }

    private Supplier<HTTPResponse> not(List<HTTPRequest.Method> allowed) {
        return new ResponseBuilder()
          .set(HTTPResponse.Status.METHOD_NOT_ALLOWED)
          .add(Headers.contentLength(0))
          .add(Headers.allow(allowed));
    }

    private Supplier<HTTPResponse> notFound() {
        return new ResponseBuilder()
          .set(HTTPResponse.Status.NOT_FOUND)
          .add((Headers.contentLength(0)));
    }

    private Function<HTTPRequest, HTTPResponse> options() {
        return (HTTPRequest request) -> new ResponseBuilder()
          .set(HTTPResponse.Status.OK)
          .add(Headers.allow(allowed(request)))
          .add(Headers.contentLength(0))
          .get();
    }
}
