package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestBuilderTest {
    @Test
    void itCanBuildAnHTTPRequest() {
        Route route = new Route(HTTPRequest.Method.GET, "/simple_get");
        String body = "name=<something>";
        String header = Headers.contentLength(body.length());

        HTTPRequest request = new RequestBuilder()
          .setRoute(route)
          .addHeader(header)
          .setBody(Optional.of(body))
          .get();

        HTTPRequest expectedRequest = new HTTPRequest(route, List.of(header), Optional.of(body));
        assertEquals(expectedRequest, request);
    }

    @Test
    void itCanBuildAnHTTPRequestWithOptionalHeadersAndBody() {
        Route route = new Route(HTTPRequest.Method.GET, "/simple_get");

        HTTPRequest request = new RequestBuilder()
          .setRoute(route)
          .get();

        HTTPRequest expectedRequest = new HTTPRequest(route, List.of(), Optional.empty());
        assertEquals(expectedRequest, request);
    }
}
