package io.github.christophermanahan.carnitas;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Request {
    private final Method method;
    private final String uri;
    private Optional<String> body = Optional.empty();

    enum Method {
        GET,
        HEAD,
        POST,
        OPTIONS
    }

    Request(Method method, String uri) {
        this.method = method;
        this.uri = uri;
    }

    private Request(Method method, String uri, Optional<String> body) {
        this.method = method;
        this.uri = uri;
        this.body = body;
    }

    Request withBody(Optional<String> body) {
        return new Request(method, uri, body);
    }

    public Method method() {
        return method;
    }

    public String uri() {
        return uri;
    }

    public Optional<String> body() {
        return body;
    }

    @Override
    public String toString() {
        return Stream.of(requestLine(), List.of(body.orElse("")))
          .flatMap(Collection::stream)
          .filter(s -> !s.isEmpty())
          .collect(Collectors.joining(Response.CRLF))
          .concat(Response.CRLF);
    }

    private List<String> requestLine() {
        return List.of(String.join(" ", method.toString(), uri));
    }
}
