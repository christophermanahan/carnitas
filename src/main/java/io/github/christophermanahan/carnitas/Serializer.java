package io.github.christophermanahan.carnitas;

import java.util.Set;

class Serializer {

    byte[] serialize(Response response) {
        return (
          statusLine(response)
          + format(response.headers())
          + Response.BLANK_LINE
          + body(response)
        ).getBytes();
    }

    private String statusLine(Response response) {
        return Response.VERSION + " " + response.status().code + Response.CRLF;
    }

    private String format(Set<String> headers) {
        return String.join(Response.CRLF, headers);
    }

    private String body(Response response) {
        return response.body().orElse("");
    }
}
