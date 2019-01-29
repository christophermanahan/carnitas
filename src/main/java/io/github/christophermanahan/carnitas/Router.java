package io.github.christophermanahan.carnitas;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class Router implements Handler {
    private final HashMap<Route, Function<HTTPRequest, HTTPResponse>> map;
    private final HashMap<Route, Function<HTTPRequest, HTTPResponse2>> map2;

    Router() {
        this.map = new HashMap<>();
        this.map2 = new HashMap<>();
    }

    Router get(String uri, Function<HTTPRequest, HTTPResponse> handler) {
        map.put(new Route(HTTPRequest.Method.GET, uri), handler);
        return this;
    }

    public Router get2(String uri, Function<HTTPRequest, HTTPResponse2> handler) {
        map2.put(new Route(HTTPRequest.Method.GET, uri), handler);
        return this;
    }

    Router head(String uri, Function<HTTPRequest, HTTPResponse> handler) {
        map.put(new Route(HTTPRequest.Method.HEAD, uri), handler);
        return this;
    }

    public Router head2(String uri, Function<HTTPRequest, HTTPResponse2> handler) {
        map2.put(new Route(HTTPRequest.Method.HEAD, uri), handler);
        return this;
    }

    Router post(String uri, Function<HTTPRequest, HTTPResponse> handler) {
        map.put(new Route(HTTPRequest.Method.POST, uri), handler);
        return this;
    }

    public Router post2(String uri, Function<HTTPRequest, HTTPResponse2> handler) {
        map2.put(new Route(HTTPRequest.Method.POST, uri), handler);
        return this;
    }

    public HTTPResponse handle(HTTPRequest request) {
        if (routeAdded(request)) {
            return handleRoute(request);
        } else if (allowedUri(request)) {
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
        return map.keySet().stream()
          .filter(route -> route.equals(route(request)))
          .findFirst()
          .map(map::get)
          .map(handler -> handler.apply(request))
          .get();
    }

    private boolean allowedUri(HTTPRequest request) {
        return map.keySet().stream()
          .anyMatch(route -> route.uri().equals(request.uri()));
    }

    private HTTPResponse methodNotAllowed(HTTPRequest request) {
        return new HTTPResponse(HTTPResponse.Status.METHOD_NOT_ALLOWED)
          .withHeaders(List.of(
            Headers.allow(allowedMethods(request)))
          );
    }

    private List<HTTPRequest.Method> allowedMethods(HTTPRequest request) {
        return map.keySet().stream()
          .filter(route -> route.uri().equals(request.uri()))
          .map(Route::method)
          .collect(Collectors.toList());
    }

    private HTTPResponse notFound() {
        return new HTTPResponse(HTTPResponse.Status.NOT_FOUND);
    }

    public HTTPResponse2 handle2(HTTPRequest request) {
        return map2.keySet().stream()
          .filter(route -> route.equals(route(request)))
          .findFirst()
          .map(map2::get)
          .map(handler -> handler.apply(request))
          .orElseGet(handler(request));
    }

    private Supplier<HTTPResponse2> handler(HTTPRequest request) {
        List<HTTPRequest.Method> allowed = allowed(request);
        return allowed.isEmpty() ? notFound2() : not(allowed);
    }

    private List<HTTPRequest.Method> allowed(HTTPRequest request) {
        return map2.keySet().stream()
          .filter(route -> route.uri().equals(request.uri()))
          .map(Route::method)
          .collect(Collectors.toList());
    }

    private Supplier<HTTPResponse2> not(List<HTTPRequest.Method> allowed) {
        return new ResponseBuilder()
          .setStatus(HTTPResponse2.Status.METHOD_NOT_ALLOWED)
          .addHeader(Headers.contentLength(0))
          .addHeader(Headers.allow(allowed));
    }

    private Supplier<HTTPResponse2> notFound2() {
        return new ResponseBuilder()
          .setStatus(HTTPResponse2.Status.NOT_FOUND)
          .addHeader(Headers.contentLength(0));
    }
}
