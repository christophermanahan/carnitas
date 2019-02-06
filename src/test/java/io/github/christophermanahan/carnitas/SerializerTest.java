package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SerializerTest {
    @Test
    void itCanSerializeAnHTTPResponseWithoutHeadersOrBody() {
        Response.Status status = Response.Status.OK;
        Serializer serializer = new Serializer();
        Response response = new ResponseBuilder()
          .set(status)
          .get();

        String serialized = new String(serializer.serialize(response));

        String expectedSerialized = Response.VERSION + " " + status.code + Serializer.CRLF
          + Serializer.BLANK_LINE;
        assertEquals(expectedSerialized, serialized);
    }

    @Test
    void itCanSerializeAnHTTPResponseWithHeadersAndWithoutBody() {
        Response.Status status = Response.Status.OK;
        String allow = Headers.ALLOW + Request.Method.GET;
        Serializer serializer = new Serializer();
        Response response = new ResponseBuilder()
          .set(status)
          .add(allow)
          .get();

        String serialized = new String(serializer.serialize(response));

        String expectedSerialized = Response.VERSION + " " + status.code + Serializer.CRLF
          + allow
          + Serializer.BLANK_LINE;
        assertEquals(expectedSerialized, serialized);
    }

    @Test
    void itCanSerializeAnHTTPResponseWithHeadersAndBody() {
        Response.Status status = Response.Status.OK;
        Optional<String> body = Optional.of("name=<something>");
        String allow = Headers.ALLOW + Request.Method.GET;
        String contentLength = Headers.CONTENT_LENGTH + body.orElse("").length();
        Serializer serializer = new Serializer();
        Response response = new ResponseBuilder()
          .set(status)
          .set(body)
          .add(contentLength)
          .add(allow)
          .get();

        String serialized = new String(serializer.serialize(response));

        String expectedSerialized = Response.VERSION + " " + status.code + Serializer.CRLF
          + allow + Serializer.CRLF
          + contentLength
          + Serializer.BLANK_LINE
          + body.orElse("");
        assertEquals(expectedSerialized, serialized);
    }
}
