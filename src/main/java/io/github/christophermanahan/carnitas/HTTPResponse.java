package io.github.christophermanahan.carnitas;

import java.util.Optional;

class HTTPResponse {
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
        return (Constants.VERSION + " " + statusCode + Constants.CRLF
          + Headers.CONTENT_LENGTH + body.orElse("").length()
          + Constants.BLANK_LINE
          + body.orElse("")
        ).getBytes();
    }
}
