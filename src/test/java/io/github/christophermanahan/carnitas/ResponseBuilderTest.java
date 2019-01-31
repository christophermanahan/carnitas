package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponseBuilderTest {
    @Test
    void itCanBuildAnHTTPResponse() {
        HTTPResponse.Status status = HTTPResponse.Status.OK;
        String body = "name=<something>";
        Headers headers = new Headers()
          .contentLength(body.length())
          .allow(List.of(HTTPRequest.Method.GET));
        ResponseBuilder builder = new ResponseBuilder();

        HTTPResponse response = builder
          .set(status)
          .set(headers)
          .set(Optional.of(body))
          .get();

        HTTPResponse expectedResponse = new HTTPResponse(status, headers, Optional.of(body));
        assertEquals(expectedResponse, response);
    }

    @Test
    void itCanBuildAnHTTPResponseWithOptionalHeadersAndBody() {
        HTTPResponse.Status status = HTTPResponse.Status.OK;
        ResponseBuilder builder = new ResponseBuilder();

        HTTPResponse response = builder
          .set(status)
          .get();

        HTTPResponse expectedResponse = new HTTPResponse(status, new Headers(), Optional.empty());
        assertEquals(expectedResponse, response);
    }
}
