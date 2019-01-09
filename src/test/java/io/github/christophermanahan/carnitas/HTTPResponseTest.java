package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HTTPResponseTest {
    @Test
    void createsGETResponseBytes() {
        Response httpResponse = new HTTPResponse();

        byte[] httpResponseBytes = httpResponse.serialize();
        byte[] expectedResponse = "HTTP/1.1 200 OK\r\nContent-Length: 0\r\n\r\n".getBytes();

        Assertions.assertArrayEquals(expectedResponse, httpResponseBytes);
    }
}
