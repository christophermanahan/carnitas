package io.github.christophermanahan.carnitas;

import java.util.Optional;

class HTTPResponse {
    private final StatusCode statusCode;
    private Optional<String> body = Optional.empty();

    static final String VERSION = "HTTP/1.1";
    static final String CRLF = "\r\n";
    static final String BLANK_LINE = "\r\n\r\n";

    enum StatusCode {
        OK ("200 OK"),
        CREATED("201 Created"),
        NOT_FOUND("404 Not Found");

        final String value;

        StatusCode(String value) {
            this.value = value;
        }
    }

    HTTPResponse(StatusCode code) {
        this.statusCode = code;
    }

    private HTTPResponse(StatusCode code, Optional<String> body) {
        this.statusCode = code;
        this.body = body;
    }

    HTTPResponse withBody(Optional<String> body) {
        return new HTTPResponse(statusCode, body);
    }

    byte[] serialize() {
        return (VERSION + " " + statusCode.value + CRLF
          + Headers.CONTENT_LENGTH + body.orElse("").length()
          + BLANK_LINE
          + body.orElse("")
        ).getBytes();
    }
}
