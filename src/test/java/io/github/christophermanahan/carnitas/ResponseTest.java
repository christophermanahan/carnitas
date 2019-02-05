package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ResponseTest {
    @Test
    void itHasAStatus() {
        Response.Status status = Response.Status.OK;

        Response response = new ResponseBuilder()
          .set(status)
          .get();

        assertEquals(status, response.status());
    }

    @Test
    void itHasHeaders() {
        String contentLength = Headers.CONTENT_LENGTH + 0;

        Response response = new ResponseBuilder()
          .add(contentLength)
          .get();

        assertEquals(Collections.singleton(contentLength), response.headers());
    }

    @Test
    void itHasABody() {
        Optional<String> body = Optional.of("name=<something>");

        Response response = new ResponseBuilder()
          .set(body)
          .get();

        assertEquals(body, response.body());
    }

    @Test
    void itCanTestEquality() {
        Response.Status status = Response.Status.OK;
        String contentLength = Headers.CONTENT_LENGTH + 0;
        Optional<String> body = Optional.of("name=<something>");
        Response response = new ResponseBuilder()
          .set(status)
          .add(contentLength)
          .set(body)
          .get();
        Response otherResponse = new ResponseBuilder()
          .set(status)
          .add(contentLength)
          .set(body)
          .get();

        assertEquals(response, otherResponse);
    }

    @Test
    void itCanTestInequalityOfStatus() {
        Response response = new ResponseBuilder()
          .set(Response.Status.OK)
          .get();
        Response otherResponse = new ResponseBuilder()
          .set(Response.Status.CREATED)
          .get();

        assertNotEquals(response, otherResponse);
    }

    @Test
    void itCanTestInequalityOfHeaders() {
        Response.Status status = Response.Status.OK;
        Response response = new ResponseBuilder()
          .set(status)
          .add(Headers.CONTENT_LENGTH + 0)
          .get();
        Response otherResponse = new ResponseBuilder()
          .set(status)
          .add(Headers.CONTENT_LENGTH + 1)
          .get();

        assertNotEquals(response, otherResponse);
    }

    @Test
    void itCanTestInequalityOfBody() {
        Response.Status status = Response.Status.OK;
        Response response = new ResponseBuilder()
          .set(status)
          .set(Optional.of("name=<something>"))
          .get();
        Response otherResponse = new ResponseBuilder()
          .set(status)
          .set(Optional.of("name=<something-else>"))
          .get();

        assertNotEquals(response, otherResponse);
    }

    @Test
    void itCanProvideAStringifiedResponse() {
        Response.Status status = Response.Status.OK;
        String contentLength = Headers.CONTENT_LENGTH + 0;
        String body = "name=<something>";
        Response response = new ResponseBuilder()
          .set(status)
          .add(contentLength)
          .set(Optional.of(body))
          .get();

        String expectedStringified = status + Serializer.CRLF
          + contentLength + Serializer.CRLF
          + body;
        Assertions.assertEquals(expectedStringified, response.toString());
    }
}
