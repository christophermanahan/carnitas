package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HTTPServerTest {
    private TestLogger logger;
    private TestParser parser;
    private TestHandler handler;

    @BeforeEach
    void setup() {
        logger = new TestLogger();
        parser = new TestParser();
        handler = new TestHandler();
    }

    @Test
    void sendsHTTPResponsesWhenTestConnectionSendsHTTPRequests() {
        String request = "GET /simple_get HTTP/1.1";
        List<TestConnection> connections = List.of(new TestConnection(request), new TestConnection(request), new TestConnection(request));
        Acceptor acceptor = new TestAcceptor(connections);

        new HTTPServer(acceptor, parser, handler, logger).run();

        String expectedResponse = new String(new TestResponse(request).serialize());
        for (TestConnection connection : connections) {
            assertEquals(expectedResponse, connection.response);
        }
    }

    @Test
    void connectionIsClosedAfterResponse() {
        String request = "GET /simple_get HTTP/1.1";
        List<TestConnection> connections = List.of(new TestConnection(request));
        Acceptor acceptor = new TestAcceptor(connections);

        new HTTPServer(acceptor, parser, handler, logger).run();

        Assertions.assertNull( connections.get(0).receiver().receiveLine());
    }

    @Test
    void logsExceptionIfAcceptFails() {
        class AcceptorException extends TestAcceptor {
            private AcceptorException(List<TestConnection> connections) {
                super(connections);
            }

            public TestConnection accept() {
                connections.next();
                throw new RuntimeException(ErrorMessages.ACCEPT_CONNECTION);
            }
        }
        String request = "GET /simple_get HTTP/1.1";
        Acceptor acceptor = new AcceptorException(List.of(new TestConnection(request)));

        new HTTPServer(acceptor, parser, handler, logger).run();

        assertEquals(ErrorMessages.ACCEPT_CONNECTION, logger.logged());
    }

    @Test
    void logsExceptionIfSendFails() {
        class SendException extends TestConnection {
            private SendException(String request) {
                super(request);
            }

            public void send(Response response) {
                throw new RuntimeException(ErrorMessages.SEND_TO_CONNECTION);
            }
        }
        String request = "GET /simple_get HTTP/1.1";
        Acceptor acceptor = new TestAcceptor(List.of(new SendException(request)));

        new HTTPServer(acceptor, parser, handler, logger).run();

        assertEquals(ErrorMessages.SEND_TO_CONNECTION, logger.logged());
    }

    @Test
    void logsExceptionIfCloseFails() {
        class CloseException extends TestConnection {
            private CloseException(String request) {
                super(request);
            }

            public void close() {
                throw new RuntimeException(ErrorMessages.CLOSE_CONNECTION);
            }
        }
        String request = "GET /simple_get HTTP/1.1";
        Acceptor acceptor = new TestAcceptor(List.of(new CloseException(request)));

        new HTTPServer(acceptor, parser, handler, logger).run();

        assertEquals(ErrorMessages.CLOSE_CONNECTION, logger.logged());
    }



    private class TestAcceptor implements Acceptor {
        final Iterator<TestConnection> connections;

        TestAcceptor(List<TestConnection> connections) {
            this.connections = connections.iterator();
        }

        public TestConnection accept() {
            return connections.next();
        }

        public void close() {}

        public boolean isAccepting() {
            return connections.hasNext();
        }
    }



    private class TestConnection implements Connection {
        private String request;
        String response;
        private boolean closed;

        TestConnection(String request) {
            this.request = request;
            this.closed = false;
        }

        public Receiver receiver() {
            return closed ? new TestReceiver(null) : new TestReceiver(request);
        }

        public void send(Response response) {
            this.response = new String(response.serialize());
        }

        public void close() {
            closed = true;
        }
    }

    private class TestReceiver implements Receiver {
        private final String request;

        TestReceiver(String request) {
            this.request = request;
        }

        public String receiveLine() {
            return request;
        }

        public String receiveCharacters(int amount) {
            return null;
        }
    }

    private class TestParser implements Parser {
        public Optional<Request> parse(Receiver receiver) {
            String received = receiver.receiveLine();
            Request request = received.isEmpty() ? null : new TestRequest(received);
            return Optional.ofNullable(request);
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

    private class TestHandler implements Handler {
        public Response handle(Request request) {
            return new TestResponse(request.body());
        }
    }

    private class TestResponse implements Response {
        private final String request;

        TestResponse(String request) {
            this.request = request;
        }

        public byte[] serialize() {
            return request.getBytes();
        }
    }

    private class TestLogger implements Logger {
        private final StringBuilder log;

        TestLogger() {
            this.log = new StringBuilder();
        }

        String logged() {
            return log.toString();
        }

        public void log(String message) {
            log.append(message);
        }
    }
}
