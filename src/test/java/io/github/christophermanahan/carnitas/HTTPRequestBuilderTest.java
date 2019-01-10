package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HTTPRequestBuilderTest {
    @Test
    void buildsHTTPRequests() {
        String requestMethod = "GET";
        String body = "name=<something>";

        Request request = new HTTPRequestBuilder()
          .requestMethod(requestMethod)
          .body(body)
          .build();

        RequestMethod expectedRequestMethod = RequestMethod.valueOf(requestMethod);
        assertEquals(expectedRequestMethod, request.requestMethod());
        assertEquals(body, request.body());
    }
}
