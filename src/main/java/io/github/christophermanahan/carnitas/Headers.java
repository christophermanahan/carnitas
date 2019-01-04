package io.github.christophermanahan.carnitas;

public class Headers {

    private static final String CONTENT_LENGTH = "Content-Length: ";

    static String contentLength(int length) {
        return CONTENT_LENGTH + length;
    }
}
