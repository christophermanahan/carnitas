package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestBuilderTest {
    @Test
    void itCanBuildAnHTTPRequest() {
        HTTPRequest.Method method = HTTPRequest.Method.GET;
        String uri = "/simple_get";
        String body = "name=<something>";
        Headers headers = new Headers().contentLength(body.length());

        HTTPRequest request = new RequestBuilder()
          .set(method)
          .set(uri)
          .set(headers)
          .set(Optional.of(body))
          .get();

        HTTPRequest expectedRequest = new HTTPRequest(new Route(method, uri), headers, Optional.of(body));
        assertEquals(expectedRequest, request);
    }

    @Test
    void itCanBuildAnHTTPRequestWithOptionalHeadersAndBody() {
        HTTPRequest.Method method = HTTPRequest.Method.GET;
        String uri = "/simple_get";

        HTTPRequest request = new RequestBuilder()
          .set(method)
          .set(uri)
          .get();

        HTTPRequest expectedRequest = new HTTPRequest(new Route(method, uri), new Headers(), Optional.empty());
        assertEquals(expectedRequest, request);
    }
}
