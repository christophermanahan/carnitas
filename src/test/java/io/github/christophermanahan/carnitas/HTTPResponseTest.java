package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HTTPResponseTest {
    @Test
    void itSerializesToAFormattedHTTPResponseWithoutBody() {
        String statusCode = "200 OK";
        HTTPResponse httpResponse = new HTTPResponse(statusCode);

        String response = new String(httpResponse.serialize());

        String expectedResponse = Constants.VERSION +  " " + statusCode + Constants.CRLF
          + Headers.CONTENT_LENGTH + 0
          + Constants.BLANK_LINE;
        assertEquals(expectedResponse, response);
    }

    @Test
    void itSerializesToAFormattedHTTPResponseWithBody() {
        String statusCode = "201 Created";
        String body = "name=<something>";
        HTTPResponse httpResponse = new HTTPResponse(statusCode)
          .withBody(Optional.of(body));

        String response = new String(httpResponse.serialize());

        String expectedResponse = Constants.VERSION +  " " + statusCode + Constants.CRLF
          + Headers.CONTENT_LENGTH + body.length()
          + Constants.BLANK_LINE
          + body;
        assertEquals(expectedResponse, response);
    }
}
