package io.github.christophermanahan.carnitas;

import java.util.Optional;

class HTTPResponse {
    static final String VERSION = "HTTP/1.1";
    static final String CRLF = "\r\n";
    static final String BLANK_LINE = "\r\n\r\n";
    private final String statusCode;
    private Optional<String> body = Optional.empty();

    HTTPResponse(String statusCode) {
        this.statusCode = statusCode;
    }

    private HTTPResponse(String statusCode, Optional<String> body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    HTTPResponse withBody(Optional<String> body) {
        return new HTTPResponse(statusCode, body);
    }

    byte[] serialize() {
        return (VERSION + " " + statusCode + CRLF
          + Headers.CONTENT_LENGTH + body.orElse("").length()
          + BLANK_LINE
          + body.orElse("")
        ).getBytes();
    }
}
