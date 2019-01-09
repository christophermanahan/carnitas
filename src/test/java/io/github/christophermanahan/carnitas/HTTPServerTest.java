package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HTTPServerTest {
    private List<String> response;
    private TestLogger logger;

    @BeforeEach
    void setup() {
        logger = new TestLogger();
    }

    @Test
    void sendsHTTPResponsesWhenTestConnectionsSendRequests() {
        String request = "GET /simple_get HTTP/1.1";
        List<TestConnection> connections = List.of(new TestConnection(request), new TestConnection(request), new TestConnection(request));
        Acceptor acceptor = new TestAcceptor(connections);

        new HTTPServer(acceptor, logger).run();

        String expectedResponse = new String(new HTTPResponse().serialize());
        for (TestConnection connection : connections) {
            assertEquals(expectedResponse, connection.response);
        }
    }

    @Test
    void connectionIsClosedAfterResponse() {
        String request = "GET /simple_get HTTP/1.1";
        List<TestConnection> connections = List.of(new TestConnection(request));
        Acceptor acceptor = new TestAcceptor(connections);

        new HTTPServer(acceptor, logger).run();

        assertEquals(Optional.empty(), connections.get(0).receive());
    }

    @Test
    void logsExceptionIfAcceptFails() {
        String request = "GET /simple_get HTTP/1.1";
        Acceptor acceptor = new AcceptorException(List.of(new TestConnection(request)));

        new HTTPServer(acceptor, logger).run();

        assertEquals(ErrorMessages.ACCEPT_CONNECTION, logger.logged());
    }

    @Test
    void logsExceptionIfSendFails() {
        String request = "GET /simple_get HTTP/1.1";
        Acceptor acceptor = new TestAcceptor(List.of(new SendException(request)));

        new HTTPServer(acceptor, logger).run();

        assertEquals(ErrorMessages.SEND_TO_CONNECTION, logger.logged());
    }

    @Test
    void logsExceptionIfCloseFails() {
        String request = "GET /simple_get HTTP/1.1";
        Acceptor acceptor = new TestAcceptor(List.of(new CloseException(request)));

        new HTTPServer(acceptor, logger).run();

        assertEquals(ErrorMessages.CLOSE_CONNECTION, logger.logged());
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

    private class TestAcceptor implements Acceptor {
        final Iterator<TestConnection> connections;

        TestAcceptor(List<TestConnection> connections) {
            this.connections = connections.iterator();
        }

        public TestConnection accept() {
            return connections.next();
        }

        public void close() {
        }

        public boolean isAccepting() {
            return connections.hasNext();
        }
    }

    private class AcceptorException extends TestAcceptor {
        AcceptorException(List<TestConnection> connections) {
            super(connections);
        }

        public TestConnection accept() {
            connections.next();
            throw new RuntimeException(ErrorMessages.ACCEPT_CONNECTION);
        }
    }

    private class TestConnection implements Connection {
        private final String request;
        String response;
        private boolean closed;

        TestConnection(String request) {
            this.request = request;
            this.closed = false;
        }

        public Optional<String> receive() {
            return closed ? Optional.empty() : Optional.of(request);
        }

        public void send(Response httpResponse) {
            response = new String(httpResponse.serialize());
        }

        public void close() {
            closed = true;
        }
    }

    private class SendException extends TestConnection {
        SendException(String request) {
            super(request);
        }

        public void send(Response response) {
            throw new RuntimeException(ErrorMessages.SEND_TO_CONNECTION);
        }
    }

    private class CloseException extends TestConnection {
        CloseException(String request) {
            super(request);
        }

        public void close() {
            throw new RuntimeException(ErrorMessages.CLOSE_CONNECTION);
        }
    }
}
