package io.github.christophermanahan.carnitas;

import java.util.List;
import java.util.Optional;

public class HTTPResponse2 {
    private final Status status;
    private final List<String> headers;
    private final Optional<String> body;

    static final String VERSION = "HTTP/1.1";

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

    HTTPResponse2(Status status, List<String> headers, Optional<String> body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    Status status() {
        return status;
    }

    public List<String> headers() {
        return headers;
    }

    public Optional<String> body() {
        return body;
    }

    boolean equals(HTTPResponse2 response) {
        return status.equals(response.status)
          && headers.equals(response.headers)
          && body.equals(response.body);
    }
}