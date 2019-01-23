package io.github.christophermanahan.carnitas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

class RequestRouter implements Router {
    private final HashMap<String, List<Route>> map;
    static final String GET = "GET";
    static final String HEAD = "HEAD";
    static final String POST = "POST";

    RequestRouter() {
        this.map = new HashMap<>();
        map.put(GET, new ArrayList<>());
        map.put(HEAD, new ArrayList<>());
        map.put(POST, new ArrayList<>());
    }

    public RequestRouter get(String uri, Function<HTTPRequest, HTTPResponse> handler) {
        map.get(GET).add(new Route(uri, handler));
        return this;
    }

    public RequestRouter head(String uri, Function<HTTPRequest, HTTPResponse> handler) {
        map.get(HEAD).add(new Route(uri, handler));
        return this;
    }

    public RequestRouter post(String uri, Function<HTTPRequest, HTTPResponse> handler) {
        map.get(POST).add(new Route(uri, handler));
        return this;
    }

    public HTTPResponse process(HTTPRequest request) {
        if (uriAdded(request)) {
            return handledResponse(request);
        } else {
            return new HTTPResponse(StatusCodes.NOT_FOUND);
        }
    }

    private HTTPResponse handledResponse(HTTPRequest request) {
        return map.get(request.method()).stream()
          .filter(matchRoute(request))
          .map(handle(request))
          .findFirst()
          .get();
    }

    private boolean uriAdded(HTTPRequest request) {
        return map.values().stream()
          .map(list -> anyMatchesUri(list, request))
          .reduce(false, (hasUri, acc) -> hasUri || acc);
    }

    private boolean anyMatchesUri(List<Route> routes, HTTPRequest request) {
        return routes.stream()
          .map(matchUri(request))
          .reduce(false, (hasUri, acc) -> hasUri || acc);
    }

    private Function<Route, Boolean> matchUri(HTTPRequest request) {
        return route -> route.uri().equals(request.uri());
    }

    private Predicate<Route> matchRoute(HTTPRequest request) {
        return route -> route.uri().equals(request.uri());
    }

    private Function<Route, HTTPResponse> handle(HTTPRequest request) {
        return route -> route.handler().apply(request);
    }
}