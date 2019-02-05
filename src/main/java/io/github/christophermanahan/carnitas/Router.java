package io.github.christophermanahan.carnitas;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class Router implements Handler {
    private final HashMap<Route, Function<Request, Response>> map;

    Router() {
        this.map = new HashMap<>();
    }

    Router get(String uri, Function<Request, Response> handler) {
        add(Request.Method.GET, uri, handler);
        return this;
    }

    Router head(String uri, Function<Request, Response> handler) {
        add(Request.Method.HEAD, uri, handler);
        return this;
    }

    Router post(String uri, Function<Request, Response> handler) {
        add(Request.Method.POST, uri, handler);
        return this;
    }

    private void add(Request.Method method, String uri, Function<Request, Response> handler) {
        map.put(new Route(method, uri), handler);
        map.putIfAbsent(new Route(Request.Method.OPTIONS, uri), options());
    }

    public Response handle(Request request) {
        return map.keySet().stream()
          .filter(matches(request))
          .findFirst()
          .map(map::get)
          .map(handler -> handler.apply(request))
          .orElseGet(handler(request));
    }

    private Predicate<Route> matches(Request request) {
        return route -> route.equals(new Route(request.method(), request.uri()));
    }

    private Supplier<Response> handler(Request request) {
        List<String> allowed = allowed(request);
        return allowed.isEmpty() ? notFound() : not(allowed);
    }

    private List<String> allowed(Request request) {
        return map.keySet().stream()
          .filter(route -> route.uri().equals(request.uri()))
          .map(Route::method)
          .map(Enum::toString)
          .sorted()
          .collect(Collectors.toList());
    }

    private Supplier<Response> not(List<String> allowed) {
        return new ResponseBuilder()
          .set(Response.Status.METHOD_NOT_ALLOWED)
          .add(Headers.CONTENT_LENGTH + 0)
          .add(Headers.ALLOW + String.join(" ", allowed));
    }

    private Supplier<Response> notFound() {
        return new ResponseBuilder()
          .set(Response.Status.NOT_FOUND)
          .add((Headers.CONTENT_LENGTH + 0));
    }

    private Function<Request, Response> options() {
        return (Request request) -> new ResponseBuilder()
          .set(Response.Status.OK)
          .add(Headers.ALLOW + String.join(" ", allowed(request)))
          .add(Headers.CONTENT_LENGTH + 0)
          .get();
    }
}
