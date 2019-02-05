package io.github.christophermanahan.carnitas;

import java.util.HashSet;

class Serializer {
    static final String CRLF = "\r\n";
    static final String BLANK_LINE = "\r\n\r\n";

    byte[] serialize(HTTPResponse response) {
        return (
          statusLine(response)
          + format(response.headers())
          + BLANK_LINE
          + body(response)
        ).getBytes();
    }

    private String statusLine(HTTPResponse response) {
        return HTTPResponse.VERSION + " " + response.status().code + Serializer.CRLF;
    }

    private String format(HashSet headers) {
        return String.join(CRLF, headers);
    }

    private String body(HTTPResponse response) {
        return response.body().orElse("");
    }
}
