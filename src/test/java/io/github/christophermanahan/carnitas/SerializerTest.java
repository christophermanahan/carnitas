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
        String header = Headers.ALLOW + HTTPRequest.Method.GET;
        Serializer serializer = new Serializer();
        HTTPResponse response = new ResponseBuilder()
          .set(status)
          .set(new Headers().allow(List.of(HTTPRequest.Method.GET)))
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
        String allow = Headers.ALLOW + HTTPRequest.Method.GET;
        String contentLength = Headers.CONTENT_LENGTH + body.orElse("").length();
        Serializer serializer = new Serializer();
        HTTPResponse response = new ResponseBuilder()
          .set(status)
          .set(body)
          .set(new Headers()
            .contentLength(body.orElse("").length())
            .allow(List.of(HTTPRequest.Method.GET))
          ).get();

        String serialized = new String(serializer.serialize(response));

        String expectedSerialized = HTTPResponse.VERSION + " " + status.code + Serializer.CRLF
          + contentLength + Serializer.CRLF
          + allow
          + Serializer.BLANK_LINE
          + body.orElse("");
        assertEquals(expectedSerialized, serialized);
    }
}
