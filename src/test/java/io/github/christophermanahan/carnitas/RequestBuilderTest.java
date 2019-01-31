package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestBuilderTest {
    @Test
    void itCanBuildAnHTTPRequest() {
        HTTPRequest.Method method = HTTPRequest.Method.GET;
        String uri = "/simple_get";
        String body = "name=<something>";
        String header = Headers.CONTENT_LENGTH + body.length();

        HTTPRequest request = new RequestBuilder()
          .setMethod(method)
          .setUri(uri)
          .addHeader(header)
          .setBody(Optional.of(body))
          .get();

        HTTPRequest expectedRequest = new HTTPRequest(new Route(method, uri), List.of(header), Optional.of(body));
        assertEquals(expectedRequest, request);
    }

    @Test
    void itCanBuildAnHTTPRequestWithOptionalHeadersAndBody() {
        HTTPRequest.Method method = HTTPRequest.Method.GET;
        String uri = "/simple_get";

        HTTPRequest request = new RequestBuilder()
          .setMethod(method)
          .setUri(uri)
          .get();

        HTTPRequest expectedRequest = new HTTPRequest(new Route(method, uri), List.of(), Optional.empty());
        assertEquals(expectedRequest, request);
    }
}
