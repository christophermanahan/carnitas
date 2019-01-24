package io.github.christophermanahan.carnitas;

class Headers {
    static final String CONTENT_LENGTH = "Content-Length: ";

    static String contentLength(Integer length) {
        return CONTENT_LENGTH + length;
    }
}
