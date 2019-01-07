package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

class RequestParserTest {

    @Test
    void parsesRequestWithoutBody() {
        String body = "";
        String request = "GET HTTP/1.1 /simple_get"
          + Constants.CRLF
          + Headers.CONTENT_LENGTH + body.length();
        Receiver receiver = new TestReceiver(request);
        Parser parser = new RequestParser();

        Optional<String> parsed = parser.parse(receiver);

        Assertions.assertEquals(Optional.of(body), parsed);
    }

    @Test
    void parsesRequestWithBody() {
        String body = "hello world";
        String request = "POST HTTP/1.1 /simple_post"
          + Constants.CRLF
          + Headers.CONTENT_LENGTH + body.length()
          + Constants.BLANK_LINE
          + body;
        Receiver receiver = new TestReceiver(request);
        Parser parser = new RequestParser();

        Optional<String> parsed = parser.parse(receiver);

        Assertions.assertEquals(Optional.of(body), parsed);
    }

    @Test
    void parseIsEmptyIfReceiveFails() {
        Receiver receiver = new FailedReceiver();
        Parser parser = new RequestParser();

        Optional<String> parsed = parser.parse(receiver);

        Assertions.assertEquals(Optional.empty(), parsed);
    }

    private class TestReceiver implements Receiver {

        private final Iterator<String> received;

        TestReceiver(String request) {
            this.received = List.of(request.split("\\r\\n")).iterator();
        }

        public String receiveLine() {
            return received.next();
        }

        public String receiveCharacters(int amount) {
            return received.next();
        }
    }

    private class FailedReceiver implements Receiver {

        public String receiveLine() {
            throw new RuntimeException();
        }

        public String receiveCharacters(int amount) {
            throw new RuntimeException();
        }
    }
}
