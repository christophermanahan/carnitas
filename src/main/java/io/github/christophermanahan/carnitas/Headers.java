package io.github.christophermanahan.carnitas;

import java.util.List;
import java.util.stream.Collectors;

class Headers {
    static final String CONTENT_LENGTH = "Content-Length: ";
    static final String ALLOW = "Allow: ";

    static String contentLength(Integer length) {
        return CONTENT_LENGTH + length;
    }

    static String allow(List<String> methods) {
        return ALLOW + methods.stream()
          .collect(Collectors.joining(" "));
    }
}
