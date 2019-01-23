package io.github.christophermanahan.carnitas;

public class Route {
    private final String method;
    private final String uri;

    Route(String method, String uri) {
        this.method = method;
        this.uri = uri;
    }

    public String method() {
        return method;
    }

    public String uri() {
        return uri;
    }

    public boolean equals(Route route) {
        return method.equals(route.method) && uri.equals(route.uri);
    }
}
