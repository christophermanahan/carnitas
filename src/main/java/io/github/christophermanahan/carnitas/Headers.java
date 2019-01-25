package io.github.christophermanahan.carnitas;

import java.util.List;

class Headers {
    static final String CONTENT_LENGTH = "Content-Length: ";
    static final String ALLOW = "Allow: ";

    static String contentLength(Integer length) {
        return CONTENT_LENGTH + length;
    }

    static String allow(List<String> methods) {
        return ALLOW + String.join(" ", methods);
    }
}
