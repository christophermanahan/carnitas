package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestParserTest {
    @Test
    void parsesGETRequestWithoutBody() {
        String requestMethod = "GET";
        String body = "";
        String request = requestMethod +  " /simple_get " + Constants.VERSION
          + Constants.BLANK_LINE
          + body;
        Receiver receiver = new TestReceiver(request);
        Parser parser = new RequestParser(new TestRequestBuilder());

        Optional<Request> parsed = parser.parse(receiver);

        String expectedRequest = requestMethod + body;
        String parsedRequest = parsed.isEmpty() ? null : parsed.get().body();
        assertEquals(expectedRequest, parsedRequest);
    }

    @Test
    void parsesGETRequestWithBody() {
        String requestMethod = "GET";
        String body = "name=<something>";
        String request = requestMethod +  " /simple_get " + Constants.VERSION
          + Constants.BLANK_LINE
          + body;
        Receiver receiver = new TestReceiver(request);
        Parser parser = new RequestParser(new TestRequestBuilder());

        Optional<Request> parsed = parser.parse(receiver);

        String expectedRequest = requestMethod;
        String parsedRequest = parsed.isEmpty() ? null : parsed.get().body();
        assertEquals(expectedRequest, parsedRequest);
    }

    @Test
    void parsesPOSTRequestWithoutBody() {
        String requestMethod = "POST";
        String body = "";
        String request = requestMethod +  " /simple_post " + Constants.VERSION + Constants.CRLF
          + Headers.CONTENT_LENGTH + body.length()
          + Constants.BLANK_LINE
          + body;
        Receiver receiver = new TestReceiver(request);
        Parser parser = new RequestParser(new TestRequestBuilder());

        Optional<Request> parsed = parser.parse(receiver);

        String expectedRequest = requestMethod + body;
        String parsedRequest = parsed.isEmpty() ? null : parsed.get().body();
        assertEquals(expectedRequest, parsedRequest);
    }

    @Test
    void parsesPOSTRequestWithBody() {
        String requestMethod = "POST";
        String body = "name=<something>";
        String request = requestMethod +  " /simple_get " + Constants.VERSION + Constants.CRLF
          + Headers.CONTENT_LENGTH + body.length()
          + Constants.BLANK_LINE
          + body;
        Receiver receiver = new TestReceiver(request);
        Parser parser = new RequestParser(new TestRequestBuilder());

        Optional<Request> parsed = parser.parse(receiver);

        String expectedRequest = requestMethod + body;
        String parsedRequest = parsed.isEmpty() ? null : parsed.get().body();
        assertEquals(expectedRequest, parsedRequest);
    }

    @Test
    void parseIsEmptyIfReceiveFails() {
        class FailedReceiver implements Receiver {
            public String receiveLine() {
                throw new RuntimeException();
            }

            public String receiveCharacters(int amount) {
                return null;
            }
        }
        Receiver receiver = new FailedReceiver();
        Parser parser = new RequestParser(new TestRequestBuilder());

        Optional<Request> parsed = parser.parse(receiver);

        assertEquals(Optional.empty(), parsed);
    }

    private class TestReceiver implements Receiver {

        private final Iterator<String> received;

        TestReceiver(String request) {
            List<String> lines = new ArrayList<>(Arrays.asList(request.split(Constants.CRLF)));
            lines.add("");
            this.received = lines.iterator();
        }

        public String receiveLine() {
            return received.next();
        }

        public String receiveCharacters(int amount) {
            return received.next();
        }
    }

    private class TestRequestBuilder implements RequestBuilder {
        private String requestMethod;
        private String body;

        public RequestBuilder requestMethod(String requestMethod) {
            this.requestMethod = requestMethod;
            return this;
        }

        public RequestBuilder body(String body) {
            this.body = body;
            return this;
        }

        public Request build() {
            return body == null ? new TestRequest(requestMethod) : new TestRequest(requestMethod + body);
        }
    }

    private class TestRequest implements Request {
        private final String request;

        TestRequest(String request) {
            this.request = request;
        }

        public RequestMethod requestMethod() {
            return null;
        }

        public String body() {
            return request;
        }
    }
}
