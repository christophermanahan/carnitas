package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HTTPResponse2Test {
    @Test
    void itHasAStatus() {
        HTTPResponse2.Status status = HTTPResponse2.Status.OK;

        HTTPResponse2 response = new ResponseBuilder()
          .setStatus(status)
          .get();

        assertEquals(status, response.status());
    }

    @Test
    void itHasHeaders() {
        String header = Headers.contentLength(0);
        List<String> headers = List.of(header);

        HTTPResponse2 response = new ResponseBuilder()
          .addHeader(header)
          .get();

        assertEquals(headers, response.headers());
    }

    @Test
    void itHasABody() {
        Optional<String> body = Optional.of("name=<something>");

        HTTPResponse2 response = new ResponseBuilder()
          .setBody(body)
          .get();

        assertEquals(body, response.body());
    }

    @Test
    void itCanTestEquality() {
        HTTPResponse2.Status status = HTTPResponse2.Status.OK;
        String header = Headers.contentLength(0);
        Optional<String> body = Optional.of("name=<something>");
        HTTPResponse2 response = new ResponseBuilder()
          .setStatus(status)
          .addHeader(header)
          .setBody(body)
          .get();
        HTTPResponse2 otherResponse = new ResponseBuilder()
          .setStatus(status)
          .addHeader(header)
          .setBody(body)
          .get();

        boolean equal = response.equals(otherResponse);

        assertTrue(equal);
    }

    @Test
    void itCanTestInequalityOfStatus() {
        HTTPResponse2 response = new ResponseBuilder()
          .setStatus(HTTPResponse2.Status.OK)
          .get();
        HTTPResponse2 otherResponse = new ResponseBuilder()
          .setStatus(HTTPResponse2.Status.CREATED)
          .get();

        boolean equal = response.equals(otherResponse);

        assertFalse(equal);
    }

    @Test
    void itCanTestInequalityOfHeaders() {
        HTTPResponse2.Status status = HTTPResponse2.Status.OK;
        HTTPResponse2 response = new ResponseBuilder()
          .setStatus(status)
          .addHeader(Headers.contentLength(0))
          .get();
        HTTPResponse2 otherResponse = new ResponseBuilder()
          .setStatus(status)
          .addHeader(Headers.contentLength(1))
          .get();

        boolean equal = response.equals(otherResponse);
        assertFalse(equal);
    }

    @Test
    void itCanTestInequalityOfBody() {
        HTTPResponse2.Status status = HTTPResponse2.Status.OK;
        HTTPResponse2 response = new ResponseBuilder()
          .setStatus(status)
          .setBody(Optional.of("name=<something>"))
          .get();
        HTTPResponse2 otherResponse = new ResponseBuilder()
          .setStatus(status)
          .setBody(Optional.of("name=<something-else>"))
          .get();

        boolean equal = response.equals(otherResponse);

        assertFalse(equal);
    }
}
