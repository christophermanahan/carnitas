package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponseBuilderTest {
    @Test
    void itCanBuildAnHTTPResponse() {
        HTTPResponse.Status status = HTTPResponse.Status.OK;
        String contentLength = Headers.contentLength(0);
        String allow = Headers.allow(List.of(HTTPRequest.Method.GET));
        List<String> headers = List.of(contentLength, allow);
        Optional<String> body = Optional.of("name=<something>");
        ResponseBuilder builder = new ResponseBuilder();

        HTTPResponse response = builder
          .setStatus(status)
          .addHeader(contentLength)
          .addHeader(allow)
          .setBody(body)
          .get();

        HTTPResponse expectedResponse = new HTTPResponse(status, headers, body);
        assertEquals(expectedResponse, response);
    }

    @Test
    void itCanBuildAnHTTPResponseWithOptionalHeadersAndBody() {
        HTTPResponse.Status status = HTTPResponse.Status.OK;
        ResponseBuilder builder = new ResponseBuilder();

        HTTPResponse response = builder
          .setStatus(status)
          .get();

        HTTPResponse expectedResponse = new HTTPResponse(status, List.of(), Optional.empty());
        assertEquals(expectedResponse, response);
    }
}
