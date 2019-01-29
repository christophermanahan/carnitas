package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SerializerTest {
    @Test
    void itCanSerializeAnHTTPResponseWithoutHeadersOrBody() {
        HTTPResponse2.Status status = HTTPResponse2.Status.OK;
        Serializer serializer = new Serializer();
        HTTPResponse2 response = new ResponseBuilder()
          .setStatus(status)
          .get();

        String serialized = new String(serializer.serialize(response));

        String expectedSerialized = HTTPResponse2.VERSION + " " + status.code + Serializer.CRLF
          + Serializer.BLANK_LINE;
        assertEquals(expectedSerialized, serialized);
    }

    @Test
    void itCanSerializeAnHTTPResponseWithHeadersAndWithoutBody() {
        HTTPResponse2.Status status = HTTPResponse2.Status.OK;
        String header = Headers.allow(List.of(HTTPRequest.Method.GET));
        Serializer serializer = new Serializer();
        HTTPResponse2 response = new ResponseBuilder()
          .setStatus(status)
          .addHeader(header)
          .get();

        String serialized = new String(serializer.serialize(response));

        String expectedSerialized = HTTPResponse2.VERSION + " " + status.code + Serializer.CRLF
          + header
          + Serializer.BLANK_LINE;
        assertEquals(expectedSerialized, serialized);
    }

    @Test
    void itCanSerializeAnHTTPResponseWithHeadersAndBody() {
        HTTPResponse2.Status status = HTTPResponse2.Status.OK;
        Optional<String> body = Optional.of("name=<something>");
        String allow = Headers.allow(List.of(HTTPRequest.Method.GET));
        String contentLength = Headers.contentLength(body.orElse("").length());
        Serializer serializer = new Serializer();
        HTTPResponse2 response = new ResponseBuilder()
          .setStatus(status)
          .addHeader(allow)
          .addHeader(contentLength)
          .setBody(body)
          .get();

        String serialized = new String(serializer.serialize(response));

        String expectedSerialized = HTTPResponse2.VERSION + " " + status.code + Serializer.CRLF
          + allow + Serializer.CRLF
          + contentLength
          + Serializer.BLANK_LINE
          + body.orElse("");
        assertEquals(expectedSerialized, serialized);
    }
}
