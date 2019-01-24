package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HTTPResponseTest {
    @Test
    void itSerializesToAFormattedHTTPResponseWithoutBody() {
        HTTPResponse.Status status = HTTPResponse.Status.OK;
        HTTPResponse httpResponse = new HTTPResponse(status);

        String response = new String(httpResponse.serialize());

        String expectedResponse = HTTPResponse.VERSION +  " " + status.code + HTTPResponse.CRLF
          + Headers.contentLength(0)
          + HTTPResponse.BLANK_LINE;
        assertEquals(expectedResponse, response);
    }

    @Test
    void itSerializesToAFormattedHTTPResponseWithBody() {
        HTTPResponse.Status status = HTTPResponse.Status.CREATED;
        String body = "name=<something>";
        HTTPResponse httpResponse = new HTTPResponse(status)
          .withBody(Optional.of(body));

        String response = new String(httpResponse.serialize());

        String expectedResponse = HTTPResponse.VERSION +  " " + status.code + HTTPResponse.CRLF
          + Headers.contentLength(body.length())
          + HTTPResponse.BLANK_LINE
          + body;
        assertEquals(expectedResponse, response);
    }
}
