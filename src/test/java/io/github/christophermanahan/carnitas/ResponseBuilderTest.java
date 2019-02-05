package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResponseBuilderTest {
    @Test
    void itCanBuildAnHTTPResponse() {
        Response.Status status = Response.Status.OK;
        String contentLength = Headers.CONTENT_LENGTH + 0;
        String allow = Headers.ALLOW + Request.Method.GET;
        TreeSet headers = new TreeSet<>(List.of(contentLength, allow));

        Optional<String> body = Optional.of("name=<something>");
        ResponseBuilder builder = new ResponseBuilder();

        Response response = builder
          .set(status)
          .add(contentLength)
          .add(allow)
          .set(body)
          .get();

        Response expectedResponse = new Response(status, headers, body);
        assertTrue(expectedResponse.equals(response));
    }

    @Test
    void itCanBuildAnHTTPResponseWithOptionalHeadersAndBody() {
        Response.Status status = Response.Status.OK;
        ResponseBuilder builder = new ResponseBuilder();

        Response response = builder
          .set(status)
          .get();

        Response expectedResponse = new Response(status, new TreeSet(), Optional.empty());
        assertEquals(expectedResponse, response);
    }
}
