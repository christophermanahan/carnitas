package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HTTPResponseTest {
    @Test
    void createsResponseBytes() {
        String statusCode = StatusCodes.GET;
        String version = Constants.VERSION;
        String body = "name=<something>";
        String headers = Headers.CONTENT_LENGTH + body.length();

        String response = new String(new HTTPResponse(statusCode, version, body, headers).serialize());

        String expectedResponse = version + " " + statusCode + Constants.CRLF
          + headers
          + Constants.BLANK_LINE
          + body;
        Assertions.assertEquals(expectedResponse, response);
    }
}
