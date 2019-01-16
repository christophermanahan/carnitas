package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class RequestHandler2Test {
    @Test
    void creates200ResponsesFromHEADRequests() {
        Handler2 handler = new RequestHandler2();

        HTTPResponse2 response = handler.handle(
          new HTTPRequest2("HEAD")
        );

        String expectedResponse = Constants.VERSION + " 200 OK" + Constants.CRLF
          + Headers.CONTENT_LENGTH + 0
          + Constants.BLANK_LINE;
        Assertions.assertEquals(expectedResponse, new String(response.serialize()));
    }

    @Test
    void creates200ResponsesFromGETRequestsWithoutBody() {
        Handler2 handler = new RequestHandler2();

        HTTPResponse2 response = handler.handle(
          new HTTPRequest2("GET")
        );

        String expectedResponse = Constants.VERSION + " 200 OK" + Constants.CRLF
          + Headers.CONTENT_LENGTH + 0
          + Constants.BLANK_LINE;
        Assertions.assertEquals(expectedResponse, new String(response.serialize()));
    }

    @Test
    void creates200ResponsesFromGETRequestsWithBody() {
        Handler2 handler = new RequestHandler2();

        HTTPResponse2 response = handler.handle(
          new HTTPRequest2("GET")
            .withBody(Optional.of("name=<something>"))
        );

        String expectedResponse = Constants.VERSION + " 200 OK" + Constants.CRLF
          + Headers.CONTENT_LENGTH + 0
          + Constants.BLANK_LINE;
        Assertions.assertEquals(expectedResponse, new String(response.serialize()));
    }

    @Test
    void creates201ResponsesFromPOSTRequestsWithoutBody() {
        Handler2 handler = new RequestHandler2();

        HTTPResponse2 response = handler.handle(
          new HTTPRequest2("POST")
        );

        String expectedResponse = Constants.VERSION + " 201 Created" + Constants.CRLF
          + Headers.CONTENT_LENGTH + 0
          + Constants.BLANK_LINE;
        Assertions.assertEquals(expectedResponse, new String(response.serialize()));
    }

    @Test
    void creates201ResponsesFromPOSTRequestsWithBody() {
        String body = "name=<something>";
        Handler2 handler = new RequestHandler2();

        HTTPResponse2 response = handler.handle(
          new HTTPRequest2("POST")
            .withBody(Optional.of(body))
        );

        String expectedResponse = Constants.VERSION + " 201 Created" + Constants.CRLF
          + Headers.CONTENT_LENGTH + body.length()
          + Constants.BLANK_LINE
          + body;
        Assertions.assertEquals(expectedResponse, new String(response.serialize()));
    }
}