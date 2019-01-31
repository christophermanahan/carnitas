package io.github.christophermanahan.carnitas;

import java.util.Iterator;
import java.util.Optional;

public class HeadersParser implements Parser<Iterator<String>, Iterator<String>> {
    public HeadersParser(RequestBuilder builder) {

    }

    public Optional<HTTPRequest> parse(Reader reader) {
        return Optional.empty();
    }

    public Iterator<String> parse(Iterator<String> request) {
        return null;
    }
}
