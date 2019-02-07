package io.github.christophermanahan.carnitas;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Response {
    private final Status status;
    private final Set<String> headers;
    private final Optional<String> body;

    static final String VERSION = "HTTP/1.1";
    static final String CRLF = "\r\n";
    static final String BLANK_LINE = "\r\n\r\n";

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

    Response(Status status, Set<String> headers, Optional<String> body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    Status status() {
        return status;
    }

    public Set<String> headers() {
        return headers;
    }

    public Optional<String> body() {
        return body;
    }

    @Override
    public boolean equals(Object object) {
        Response response = (Response) object;
        return status.equals(response.status)
          && headers.equals(response.headers)
          && body.equals(response.body);
    }

    @Override
    public String toString() {
        return Stream.of(statusLine(), headers, List.of(body.orElse("")))
          .flatMap(Collection::stream)
          .filter(s -> !s.isEmpty())
          .collect(Collectors.joining(CRLF))
          .concat(CRLF);
    }

    private List<String> statusLine() {
        return List.of(String.join(" ", VERSION, status.code));
    }
}
