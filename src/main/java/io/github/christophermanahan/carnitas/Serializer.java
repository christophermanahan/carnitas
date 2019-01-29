package io.github.christophermanahan.carnitas;

import java.util.List;

class Serializer {
    static final String CRLF = "\r\n";
    static final String BLANK_LINE = "\r\n\r\n";

    byte[] serialize(HTTPResponse2 response) {
        return (
          statusLine(response)
          + format(response.headers())
          + BLANK_LINE
          + body(response)
        ).getBytes();
    }

    private String statusLine(HTTPResponse2 response) {
        return HTTPResponse2.VERSION + " " + response.status().code + Serializer.CRLF;
    }

    private String format(List<String> headers) {
        return String.join(CRLF, headers);
    }

    private String body(HTTPResponse2 response) {
        return response.body().orElse("");
    }
}
