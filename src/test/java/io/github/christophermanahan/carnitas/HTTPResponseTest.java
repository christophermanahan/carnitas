package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HTTPResponseTest {
    @Test
    void itSerializesToAFormattedHTTPResponseWithoutBody() {
        HTTPResponse.StatusCode statusCode = HTTPResponse.StatusCode.OK;
        HTTPResponse httpResponse = new HTTPResponse(statusCode);

        String response = new String(httpResponse.serialize());

        String expectedResponse = HTTPResponse.VERSION +  " " + statusCode.value + HTTPResponse.CRLF
          + Headers.contentLength(0)
          + HTTPResponse.BLANK_LINE;
        assertEquals(expectedResponse, response);
    }

    @Test
    void itSerializesToAFormattedHTTPResponseWithBody() {
        HTTPResponse.StatusCode statusCode = HTTPResponse.StatusCode.CREATED;
        String body = "name=<something>";
        HTTPResponse httpResponse = new HTTPResponse(statusCode)
          .withBody(Optional.of(body));

        String response = new String(httpResponse.serialize());

        String expectedResponse = HTTPResponse.VERSION +  " " + statusCode.value + HTTPResponse.CRLF
          + Headers.contentLength(body.length())
          + HTTPResponse.BLANK_LINE
          + body;
        assertEquals(expectedResponse, response);
    }
}
