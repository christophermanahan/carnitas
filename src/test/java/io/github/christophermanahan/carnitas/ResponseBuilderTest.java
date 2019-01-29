package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ResponseBuilderTest {
    @Test
    void itCanBuildAnHTTPResponse() {
        HTTPResponse2.Status status = HTTPResponse2.Status.OK;
        String contentLength = Headers.contentLength(0);
        String allow = Headers.allow(List.of(HTTPRequest.Method.GET));
        List<String> headers = List.of(contentLength, allow);
        Optional<String> body = Optional.of("name=<something>");
        ResponseBuilder builder = new ResponseBuilder();

        HTTPResponse2 response = builder.setStatus(status)
          .addHeader(contentLength)
          .addHeader(allow)
          .setBody(body)
          .get();

        HTTPResponse2 expectedResponse = new HTTPResponse2(status, headers, body);
        assertTrue(expectedResponse.equals(response));
    }

    @Test
    void itCanBuildAnHTTPResponseWithOptionalHeadersAndBody() {
        HTTPResponse2.Status status = HTTPResponse2.Status.OK;
        ResponseBuilder builder = new ResponseBuilder();

        HTTPResponse2 response = builder.setStatus(status)
          .get();

        HTTPResponse2 expectedResponse = new HTTPResponse2(status, List.of(), Optional.empty());
        assertTrue(expectedResponse.equals(response));
    }
}
