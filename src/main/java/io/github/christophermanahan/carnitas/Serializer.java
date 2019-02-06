package io.github.christophermanahan.carnitas;

import java.util.Set;

class Serializer {
    static final String CRLF = "\r\n";
    static final String BLANK_LINE = "\r\n\r\n";

    byte[] serialize(Response response) {
        return (
          statusLine(response)
          + format(response.headers())
          + BLANK_LINE
          + body(response)
        ).getBytes();
    }

    private String statusLine(Response response) {
        return Response.VERSION + " " + response.status().code + Serializer.CRLF;
    }

    private String format(Set<String> headers) {
        return String.join(CRLF, headers);
    }

    private String body(Response response) {
        return response.body().orElse("");
    }
}
