package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HTTPResponseBuilderTest {
    @Test
    void buildsGETResponses() {
        String statusCode = StatusCodes.GET;
        String version = Constants.VERSION;

        Response response = new HTTPResponseBuilder()
          .statusCode(statusCode)
          .version(version)
          .build();

        String expectedResponse = statusCode + " " + version + Constants.CRLF
          + Constants.BLANK_LINE;
        assertEquals(expectedResponse, new String(response.serialize()));
    }

    @Test
    void buildsPOSTResponses() {
        String statusCode = StatusCodes.POST;
        String version = Constants.VERSION;
        String body = "name=<something>";

        Response response = new HTTPResponseBuilder()
          .statusCode(statusCode)
          .version(version)
          .body(body)
          .build();

        String expectedResponse = statusCode + " " + version + Constants.CRLF
          + Headers.CONTENT_LENGTH + body.length()
          + Constants.BLANK_LINE
          + body;
        assertEquals(expectedResponse, new String(response.serialize()));
    }
}