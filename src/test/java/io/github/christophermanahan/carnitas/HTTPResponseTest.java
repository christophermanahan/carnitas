package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HTTPResponseTest {

  @Test
  void createsGETResponseBytes() {
    Response httpResponse = new HTTPResponse("data");

    byte[] httpResponseBytes = httpResponse.bytes();

    String response = String.format("%s %s %s", HTTPResponse.VERSION, HTTPResponse.GET, HTTPResponse.CRLF);
    Assertions.assertArrayEquals(response.getBytes(), httpResponseBytes);
  }
}
