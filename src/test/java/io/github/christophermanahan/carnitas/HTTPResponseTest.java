package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class HTTPResponseTest {
    @Test
    void itHasAStatus() {
        HTTPResponse.Status status = HTTPResponse.Status.OK;

        HTTPResponse response = new ResponseBuilder()
          .setStatus(status)
          .get();

        assertEquals(status, response.status());
    }

    @Test
    void itHasHeaders() {
        String contentLength = Headers.contentLength(0);

        HTTPResponse response = new ResponseBuilder()
          .addHeader(contentLength)
          .get();

        assertEquals(List.of(contentLength), response.headers());
    }

    @Test
    void itHasABody() {
        Optional<String> body = Optional.of("name=<something>");

        HTTPResponse response = new ResponseBuilder()
          .setBody(body)
          .get();

        assertEquals(body, response.body());
    }

    @Test
    void itCanTestEquality() {
        HTTPResponse.Status status = HTTPResponse.Status.OK;
        String contentLength = Headers.contentLength(0);
        Optional<String> body = Optional.of("name=<something>");
        HTTPResponse response = new ResponseBuilder()
          .setStatus(status)
          .addHeader(contentLength)
          .setBody(body)
          .get();
        HTTPResponse otherResponse = new ResponseBuilder()
          .setStatus(status)
          .addHeader(contentLength)
          .setBody(body)
          .get();

        assertEquals(response, otherResponse);
    }

    @Test
    void itCanTestInequalityOfStatus() {
        HTTPResponse response = new ResponseBuilder()
          .setStatus(HTTPResponse.Status.OK)
          .get();
        HTTPResponse otherResponse = new ResponseBuilder()
          .setStatus(HTTPResponse.Status.CREATED)
          .get();

        assertNotEquals(response, otherResponse);
    }

    @Test
    void itCanTestInequalityOfHeaders() {
        HTTPResponse.Status status = HTTPResponse.Status.OK;
        HTTPResponse response = new ResponseBuilder()
          .setStatus(status)
          .addHeader(Headers.contentLength(0))
          .get();
        HTTPResponse otherResponse = new ResponseBuilder()
          .setStatus(status)
          .addHeader(Headers.contentLength(1))
          .get();

        assertNotEquals(response, otherResponse);
    }

    @Test
    void itCanTestInequalityOfBody() {
        HTTPResponse.Status status = HTTPResponse.Status.OK;
        HTTPResponse response = new ResponseBuilder()
          .setStatus(status)
          .setBody(Optional.of("name=<something>"))
          .get();
        HTTPResponse otherResponse = new ResponseBuilder()
          .setStatus(status)
          .setBody(Optional.of("name=<something-else>"))
          .get();

        assertNotEquals(response, otherResponse);
    }
}
