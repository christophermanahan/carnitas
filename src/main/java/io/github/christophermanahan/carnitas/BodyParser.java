package io.github.christophermanahan.carnitas;

import java.util.Iterator;
import java.util.Optional;

public class BodyParser implements Parser<Iterator<String>, RequestBuilder> {
    private final RequestBuilder builder;

    public BodyParser(RequestBuilder builder) {
        this.builder = builder;
    }

    public Optional<HTTPRequest> parse(Reader reader) {
        return Optional.empty();
    }

    public RequestBuilder parse(Iterator<String> request) {
        return builder.set(Optional.of(request)
          .filter(Iterator::hasNext)
          .map(Iterator::next)
        );
    }
}
