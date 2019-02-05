package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestParserTest {
    @Test
    void itParsesRequestWithoutBody() {
        Request.Method method = Request.Method.GET;
        String uri = "/simple_get";
        String request = method + " " + uri + " " + Response.VERSION
          + Serializer.BLANK_LINE;
        Reader reader = new RequestReader(request);
        Parser parser = new RequestParser();

        Optional<Request> parsed = parser.parse(reader);

        Optional<Request.Method> parsedMethod = parsed.map(Request::method);
        Optional<String> parsedUri = parsed.map(Request::uri);
        assertEquals(Optional.of(method), parsedMethod);
        assertEquals(Optional.of(uri), parsedUri);
    }

    @Test
    void itParsesRequestWithBody() {
        Request.Method method = Request.Method.GET;
        String uri = "/simple_get";
        String body = "name=<something>";
        String request = method + " " + uri + " " + Response.VERSION + Serializer.CRLF
          + Headers.CONTENT_LENGTH + body.length() + Serializer.CRLF
          + "Test-Header: Test"
          + Serializer.BLANK_LINE
          + body;
        Reader reader = new RequestReader(request);
        Parser parser = new RequestParser();

        Optional<Request> parsed = parser.parse(reader);

        Optional<Request.Method> parsedMethod = parsed.map(Request::method);
        Optional<String> parsedUri = parsed.map(Request::uri);
        Optional<String> parsedBody = parsed.flatMap(Request::body);
        assertEquals(Optional.of(method), parsedMethod);
        assertEquals(Optional.of(uri), parsedUri);
        assertEquals(Optional.of(body), parsedBody);
    }

    @Test
    void itIsEmptyIfReaderIsEmpty() {
        Reader reader = new Reader() {
            public Optional<String> readUntil(String delimiter) {
                return Optional.empty();
            }

            public Optional<String> read(int numberOfCharacters) {
                return Optional.empty();
            }
        };
        Parser parser = new RequestParser();

        Optional<Request> parsed = parser.parse(reader);

        assertEquals(Optional.empty(), parsed);
    }

    private class RequestReader implements Reader {
        private final Iterator<String> request;

        RequestReader(String request) {
            int minimumCharacterLimit = -1;
            this.request = List.of(request.split(Serializer.CRLF, minimumCharacterLimit)).iterator();
        }

        public Optional<String> readUntil(String delimiter) {
            return Optional.of(request.next());
        }

        public Optional<String> read(int numberOfCharacters) {
            return Optional.of(request.next().substring(0, numberOfCharacters));
        }
    }
}
