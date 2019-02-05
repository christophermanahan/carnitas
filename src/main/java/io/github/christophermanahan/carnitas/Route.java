package io.github.christophermanahan.carnitas;

import java.util.Objects;

public class Route {
    private final HTTPRequest.Method method;
    private final String uri;

    Route(HTTPRequest.Method method, String uri) {
        this.method = method;
        this.uri = uri;
    }

    public HTTPRequest.Method method() {
        return method;
    }

    public String uri() {
        return uri;
    }

    @Override
    public boolean equals(Object object) {
        Route route = (Route) object;
        return method.equals(route.method) && uri.equals(route.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, uri);
    }
}
