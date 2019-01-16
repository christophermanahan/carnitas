package io.github.christophermanahan.carnitas;

import java.util.Optional;

public class HTTPResponse2 implements Response {
    private final String statusCode;
    private Optional<String> body = Optional.empty();

    HTTPResponse2(String statusCode) {
        this.statusCode = statusCode;
    }

    private HTTPResponse2(String statusCode, Optional<String> body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    HTTPResponse2 withBody(Optional<String> body) {
        return new HTTPResponse2(statusCode, body);
    }

    public byte[] serialize() {
        return (Constants.VERSION + " " + statusCode + Constants.CRLF
          + Headers.CONTENT_LENGTH + body.orElse("").length()
          + Constants.BLANK_LINE
          + body.orElse("")
        ).getBytes();
    }
}
