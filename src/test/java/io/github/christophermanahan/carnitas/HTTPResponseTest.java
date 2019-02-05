package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class HTTPResponseTest {
    @Test
    void itHasAStatus() {
        HTTPResponse.Status status = HTTPResponse.Status.OK;

        HTTPResponse response = new ResponseBuilder()
          .set(status)
          .get();

        assertEquals(status, response.status());
    }

    @Test
    void itHasHeaders() {
        String contentLength = Headers.CONTENT_LENGTH + 0;

        HTTPResponse response = new ResponseBuilder()
          .add(contentLength)
          .get();

        assertEquals(Collections.singleton(contentLength), response.headers());
    }

    @Test
    void itHasABody() {
        Optional<String> body = Optional.of("name=<something>");

        HTTPResponse response = new ResponseBuilder()
          .set(body)
          .get();

        assertEquals(body, response.body());
    }

    @Test
    void itCanTestEquality() {
        HTTPResponse.Status status = HTTPResponse.Status.OK;
        String contentLength = Headers.CONTENT_LENGTH + 0;
        Optional<String> body = Optional.of("name=<something>");
        HTTPResponse response = new ResponseBuilder()
          .set(status)
          .add(contentLength)
          .set(body)
          .get();
        HTTPResponse otherResponse = new ResponseBuilder()
          .set(status)
          .add(contentLength)
          .set(body)
          .get();

        assertEquals(response, otherResponse);
    }

    @Test
    void itCanTestInequalityOfStatus() {
        HTTPResponse response = new ResponseBuilder()
          .set(HTTPResponse.Status.OK)
          .get();
        HTTPResponse otherResponse = new ResponseBuilder()
          .set(HTTPResponse.Status.CREATED)
          .get();

        assertNotEquals(response, otherResponse);
    }

    @Test
    void itCanTestInequalityOfHeaders() {
        HTTPResponse.Status status = HTTPResponse.Status.OK;
        HTTPResponse response = new ResponseBuilder()
          .set(status)
          .add(Headers.CONTENT_LENGTH + 0)
          .get();
        HTTPResponse otherResponse = new ResponseBuilder()
          .set(status)
          .add(Headers.CONTENT_LENGTH + 1)
          .get();

        assertNotEquals(response, otherResponse);
    }

    @Test
    void itCanTestInequalityOfBody() {
        HTTPResponse.Status status = HTTPResponse.Status.OK;
        HTTPResponse response = new ResponseBuilder()
          .set(status)
          .set(Optional.of("name=<something>"))
          .get();
        HTTPResponse otherResponse = new ResponseBuilder()
          .set(status)
          .set(Optional.of("name=<something-else>"))
          .get();

        assertNotEquals(response, otherResponse);
    }
}
