package io.github.christophermanahan.carnitas;

public class Headers {

    static final String CONTENT_LENGTH = "Content-Length: ";

    public static String contentLength(int length) {
        return CONTENT_LENGTH + length;
    }
}
