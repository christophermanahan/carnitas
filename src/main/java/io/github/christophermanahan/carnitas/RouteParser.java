package io.github.christophermanahan.carnitas;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class RouteParser implements Parser<Iterator<String>, Iterator<String>> {
    private final RequestBuilder builder;

    RouteParser(RequestBuilder builder) {
        this.builder = builder;
    }

    public Optional<HTTPRequest> parse(Reader reader) {
        return Optional.empty();
    }

    public Iterator<String> parse(Iterator<String> request) {
        String requestLine = request.next();
        method(requestLine);
        uri(requestLine);
        return request;
    }

    private void method(String requestLine) {
        split(requestLine)
          .map(list -> list.get(0))
          .map(HTTPRequest.Method::valueOf)
          .findFirst()
          .map(builder::setMethod);
    }

    private void uri(String requestLine) {
        split(requestLine)
          .map(list -> list.get(1))
          .findFirst()
          .map(builder::setUri);
    }

    private Stream<List<String>> split(String requestLine) {
        return Stream.of(requestLine)
          .map(s -> s.split(" "))
          .map(Arrays::asList);
    }
}
