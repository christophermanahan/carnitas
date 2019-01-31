package io.github.christophermanahan.carnitas;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class Headers {
    private final HashSet<String> headers;

    static final String CONTENT_LENGTH = "Content-Length: ";
    static final String ALLOW = "Allow: ";

    Headers() {
        this.headers = new HashSet<>();
    }

    Headers contentLength(Integer length) {
        headers.add(CONTENT_LENGTH + length);
        return this;
    }

    Headers allow(List<HTTPRequest.Method> methods) {
        headers.add(ALLOW + methods.stream()
          .map(Enum::toString)
          .collect(Collectors.joining(" "))
        );
        return this;
    }

    Headers add(String header) {
        headers.add(header);
        return this;
    }

    public boolean equals(Object object) {
        Headers headers = (Headers) object;
        return this.headers.equals(headers.headers);
    }

    Set<String> get() {
        return headers;
    }
}
