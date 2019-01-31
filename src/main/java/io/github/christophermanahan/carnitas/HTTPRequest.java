package io.github.christophermanahan.carnitas;

import java.util.Optional;

public class HTTPRequest {
    private Method method;
    private String uri;
    private Route route;
    private Headers headers;
    private Optional<String> body;

    enum Method {
        GET,
        HEAD,
        POST
    }

    HTTPRequest(Method method, String uri) {
        this.method = method;
        this.uri = uri;
    }

    private HTTPRequest(Method method, String uri, Optional<String> body) {
        this.method = method;
        this.uri = uri;
        this.body = body;
    }

    HTTPRequest(Route route, Headers headers, Optional<String> body) {
        this.route = route;
        this.headers = headers;
        this.body = body;
    }

    HTTPRequest withBody(Optional<String> body) {
        return new HTTPRequest(method, uri, body);
    }

    public Method method() {
        return method;
    }

    public String uri() {
        return uri;
    }

    public Route route() {
        return route;
    }

    public Headers headers() {
        return headers;
    }

    public Optional<String> body() {
        return body;
    }

    public boolean equals(Object object) {
        HTTPRequest request = (HTTPRequest) object;
        return route.equals(request.route)
          && headers.equals(request.headers)
          && body.equals(request.body);
    }
}
