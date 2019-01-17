package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class RequestHandlerTest {
    @Test
    void creates200ResponsesFromHEADRequests() {
        Handler handler = new RequestHandler();

        HTTPResponse response = handler.handle(
          new HTTPRequest("HEAD")
        );

        String expectedResponse = Constants.VERSION + " 200 OK" + Constants.CRLF
          + Headers.CONTENT_LENGTH + 0
          + Constants.BLANK_LINE;
        Assertions.assertEquals(expectedResponse, new String(response.serialize()));
    }

    @Test
    void creates200ResponsesFromGETRequestsWithoutBody() {
        Handler handler = new RequestHandler();

        HTTPResponse response = handler.handle(
          new HTTPRequest("GET")
        );

        String expectedResponse = Constants.VERSION + " 200 OK" + Constants.CRLF
          + Headers.CONTENT_LENGTH + 0
          + Constants.BLANK_LINE;
        Assertions.assertEquals(expectedResponse, new String(response.serialize()));
    }

    @Test
    void creates200ResponsesFromGETRequestsWithBody() {
        Handler handler = new RequestHandler();

        HTTPResponse response = handler.handle(
          new HTTPRequest("GET")
            .withBody(Optional.of("name=<something>"))
        );

        String expectedResponse = Constants.VERSION + " 200 OK" + Constants.CRLF
          + Headers.CONTENT_LENGTH + 0
          + Constants.BLANK_LINE;
        Assertions.assertEquals(expectedResponse, new String(response.serialize()));
    }

    @Test
    void creates201ResponsesFromPOSTRequestsWithoutBody() {
        Handler handler = new RequestHandler();

        HTTPResponse response = handler.handle(
          new HTTPRequest("POST")
        );

        String expectedResponse = Constants.VERSION + " 201 Created" + Constants.CRLF
          + Headers.CONTENT_LENGTH + 0
          + Constants.BLANK_LINE;
        Assertions.assertEquals(expectedResponse, new String(response.serialize()));
    }

    @Test
    void creates201ResponsesFromPOSTRequestsWithBody() {
        String body = "name=<something>";
        Handler handler = new RequestHandler();

        HTTPResponse response = handler.handle(
          new HTTPRequest("POST")
            .withBody(Optional.of(body))
        );

        String expectedResponse = Constants.VERSION + " 201 Created" + Constants.CRLF
          + Headers.CONTENT_LENGTH + body.length()
          + Constants.BLANK_LINE
          + body;
        Assertions.assertEquals(expectedResponse, new String(response.serialize()));
    }
}