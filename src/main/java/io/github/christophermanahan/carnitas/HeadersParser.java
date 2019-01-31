package io.github.christophermanahan.carnitas;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;

public class HeadersParser implements Parser<Iterator<String>, Iterator<String>> {
    private final RequestBuilder builder;

    public HeadersParser(RequestBuilder builder) {
        this.builder = builder;
    }

    public Optional<HTTPRequest> parse(Reader reader) {
        return Optional.empty();
    }

    public Iterator<String> parse(Iterator<String> request) {
        Headers headers = new Headers();
        addHeaders(request, headers);
        builder.set(headers);
        System.out.println(builder.get().headers().get().size());
        return request;
    }

    private void addHeaders(Iterator<String> request, Headers headers) {
        if (addHeader(request, headers).isPresent()) {
            addHeaders(request, headers);
        }
    }

    private Optional<Headers> addHeader(Iterator<String> request, Headers headers) {
        return Optional.of(request)
          .map(Iterator::next)
          .filter(isPresent())
          .map(headers::add);
    }

    private Predicate<String> isPresent() {
        return header -> !header.isEmpty();
    }
}
