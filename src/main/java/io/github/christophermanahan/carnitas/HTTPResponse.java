package io.github.christophermanahan.carnitas;

import java.util.List;
import java.util.Optional;

class HTTPResponse {
    private final Status status;
    private List<String> headers = List.of();
    private Optional<String> body = Optional.empty();

    static final String VERSION = "HTTP/1.1";
    static final String CRLF = "\r\n";
    static final String BLANK_LINE = "\r\n\r\n";


    enum Status {
        OK ("200 OK"),
        CREATED("201 Created"),
        NOT_FOUND("404 Not Found"),
        METHOD_NOT_ALLOWED("405 Method Not Allowed");

        final String code;

        Status(String code) {
            this.code = code;
        }
    }

    HTTPResponse(Status status) {
        this.status = status;
    }

    private HTTPResponse(Status status, List<String> headers, Optional<String> body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    HTTPResponse withHeaders(List<String> headers) {
        return new HTTPResponse(status, headers, body);
    }

    HTTPResponse withBody(Optional<String> body) {
        return new HTTPResponse(status, headers, body);
    }

    byte[] serialize() {
        return (VERSION + " " + status.code + CRLF
          + headers()
          + BLANK_LINE
          + body.orElse("")
        ).getBytes();
    }

    private String headers() {
        if (headers.isEmpty()) {
            return Headers.contentLength(body.orElse("").length());
        } else {
            return Headers.contentLength(body.orElse("").length()) + CRLF
              + String.join(CRLF, headers);
        }
    }
}
