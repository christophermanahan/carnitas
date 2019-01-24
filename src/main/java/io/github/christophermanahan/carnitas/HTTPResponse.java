package io.github.christophermanahan.carnitas;

import java.util.Optional;

class HTTPResponse {
    private final Status status;
    private Optional<String> body = Optional.empty();

    static final String VERSION = "HTTP/1.1";
    static final String CRLF = "\r\n";
    static final String BLANK_LINE = "\r\n\r\n";

    enum Status {
        OK ("200 OK"),
        CREATED("201 Created"),
        NOT_FOUND("404 Not Found");

        final String code;

        Status(String code) {
            this.code = code;
        }
    }

    HTTPResponse(Status status) {
        this.status = status;
    }

    private HTTPResponse(Status status, Optional<String> body) {
        this.status = status;
        this.body = body;
    }

    HTTPResponse withBody(Optional<String> body) {
        return new HTTPResponse(status, body);
    }

    byte[] serialize() {
        return (VERSION + " " + status.code + CRLF
          + Headers.contentLength(body.orElse("").length())
          + BLANK_LINE
          + body.orElse("")
        ).getBytes();
    }
}
