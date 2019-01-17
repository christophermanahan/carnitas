package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestParserTest {
    @Test
    void itParsesRequestWithoutBody() {
        String method = "GET";
        String request = method + " /simple_get " + Constants.VERSION
          + Constants.BLANK_LINE;
        Reader reader = new RequestReader(request);
        Parser parser = new RequestParser();

        Optional<HTTPRequest> parsed = parser.parse(reader);

        Optional<String> parsedRequest = parsed.map(HTTPRequest::method);
        assertEquals(Optional.of(method), parsedRequest);
    }

    @Test
    void itParsesRequestWithBody() {
        String method = "GET";
        String body = "name=<something>";
        String request = method + " /simple_get " + Constants.VERSION + Constants.CRLF
          + Headers.CONTENT_LENGTH + body.length() + Constants.CRLF
          + "Test-Header: Test"
          + Constants.BLANK_LINE
          + body;
        Reader reader = new RequestReader(request);
        Parser parser = new RequestParser();

        Optional<HTTPRequest> parsed = parser.parse(reader);

        Optional<String> parsedMethod = parsed.map(HTTPRequest::method);
        Optional<String> parsedBody = parsed.flatMap(HTTPRequest::body);
        assertEquals(Optional.of(method), parsedMethod);
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

        Optional<HTTPRequest> parsed = parser.parse(reader);

        assertEquals(Optional.empty(), parsed);
    }

    private class RequestReader implements Reader {
        private final String request;
        private int index;

        RequestReader(String request) {
            this.request = request;
            this.index = -1;
        }

        public Optional<String> readUntil(String delimiter) {
            index++;
            return Optional.of(
              List.of(request.split(delimiter, -1)).get(index)
            );
        }

        public Optional<String> read(int numberOfCharacters) {
            index++;
            return Optional.of(
              List.of(request.split(Constants.CRLF, -1))
                .get(index)
                .substring(0, numberOfCharacters)
            );
        }
    }
}
