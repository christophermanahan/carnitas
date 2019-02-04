package io.github.christophermanahan.carnitas;

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

    public boolean equals(Object object) {
        Route route = (Route) object;
        return method.equals(route.method) && uri.equals(route.uri);
    }

    public int hashCode() {
        return 0;
    }
}
