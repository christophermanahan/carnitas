package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SerializerTest {
    @Test
    void itCanSerializeAnHTTPResponseWithoutHeadersOrBody() {
        HTTPResponse.Status status = HTTPResponse.Status.OK;
        Serializer serializer = new Serializer();
        HTTPResponse response = new ResponseBuilder()
          .set(status)
          .get();

        String serialized = new String(serializer.serialize(response));

        String expectedSerialized = HTTPResponse.VERSION + " " + status.code + Serializer.CRLF
          + Serializer.BLANK_LINE;
        assertEquals(expectedSerialized, serialized);
    }

    @Test
    void itCanSerializeAnHTTPResponseWithHeadersAndWithoutBody() {
        HTTPResponse.Status status = HTTPResponse.Status.OK;
        String header = Headers.allow(List.of(HTTPRequest.Method.GET));
        Serializer serializer = new Serializer();
        HTTPResponse response = new ResponseBuilder()
          .set(status)
          .set(List.of(header))
          .get();

        String serialized = new String(serializer.serialize(response));

        String expectedSerialized = HTTPResponse.VERSION + " " + status.code + Serializer.CRLF
          + header
          + Serializer.BLANK_LINE;
        assertEquals(expectedSerialized, serialized);
    }

    @Test
    void itCanSerializeAnHTTPResponseWithHeadersAndBody() {
        HTTPResponse.Status status = HTTPResponse.Status.OK;
        Optional<String> body = Optional.of("name=<something>");
        String allow = Headers.allow(List.of(HTTPRequest.Method.GET));
        String contentLength = Headers.contentLength(body.orElse("").length());
        Serializer serializer = new Serializer();
        HTTPResponse response = new ResponseBuilder()
          .set(status)
          .set(body)
          .set(List.of(
            allow,
            contentLength
          )).get();

        String serialized = new String(serializer.serialize(response));

        String expectedSerialized = HTTPResponse.VERSION + " " + status.code + Serializer.CRLF
          + allow + Serializer.CRLF
          + contentLength
          + Serializer.BLANK_LINE
          + body.orElse("");
        assertEquals(expectedSerialized, serialized);
    }
}
