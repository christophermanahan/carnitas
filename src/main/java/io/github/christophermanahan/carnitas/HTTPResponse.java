package io.github.christophermanahan.carnitas;

import java.util.Optional;
import java.util.TreeSet;

public class HTTPResponse {
    private final Status status;
    private final TreeSet headers;
    private final Optional<String> body;

    static final String VERSION = "HTTP/1.1";

    enum Status {
        OK("200 OK"),
        CREATED("201 Created"),
        NOT_FOUND("404 Not Found"),
        METHOD_NOT_ALLOWED("405 Method Not Allowed");

        final String code;

        Status(String code) {
            this.code = code;
        }
    }

    HTTPResponse(Status status, TreeSet headers, Optional<String> body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    Status status() {
        return status;
    }

    public TreeSet headers() {
        return headers;
    }

    public Optional<String> body() {
        return body;
    }

    @Override
    public boolean equals(Object object) {
        HTTPResponse response = (HTTPResponse) object;
        return status.equals(response.status)
          && headers.equals(response.headers)
          && body.equals(response.body);
    }
}
