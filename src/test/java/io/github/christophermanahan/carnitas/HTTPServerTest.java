package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HTTPServerTest {
    private List<String> responses;
    private TestLogger logger;

    @BeforeEach
    void setup() {
        responses = new ArrayList<>();
        logger = new TestLogger();
    }

    @Test
    void sendsHTTPResponsesWhenTestConnectionsSendRequests() {
        String request = "GET /simple_get HTTP/1.1";
        TestConnection connection = new TestConnection(request, responses);
        List<TestConnection> connections = List.of(connection, connection, connection);
        Acceptor acceptor = new TestAcceptor(connections);

        new HTTPServer(acceptor, logger).run();

        String response = new String(new HTTPResponse().serialize());
        List<String> responses = List.of(response, response, response);
        assertEquals(responses, responses);
    }

    @Test
    void connectionIsClosedAfterResponse() {
        String request = "GET /simple_get HTTP/1.1";
        List<TestConnection> connections = List.of(new TestConnection(request, responses));
        Acceptor acceptor = new TestAcceptor(connections);

        new HTTPServer(acceptor, logger).run();

        assertEquals(Optional.empty(), connections.get(0).receive());
    }

    @Test
    void logsExceptionIfAcceptFails() {
        String request = "GET /simple_get HTTP/1.1";
        List<TestConnection> connections = List.of(new TestConnection(request, responses));
        Acceptor acceptor = new AcceptorException(connections);

        new HTTPServer(acceptor, logger).run();

        assertEquals(ErrorMessages.ACCEPT_CONNECTION, logger.logged());
    }

    @Test
    void logsExceptionIfSendFails() {
        String request = "GET /simple_get HTTP/1.1";
        List<TestConnection> connection = List.of(new SendException(request, responses));
        Acceptor acceptor = new TestAcceptor(connection);

        new HTTPServer(acceptor, logger).run();

        assertEquals(ErrorMessages.SEND_TO_CONNECTION, logger.logged());
    }

    @Test
    void logsExceptionIfCloseFails() {
        String request = "GET /simple_get HTTP/1.1";
        List<TestConnection> connection = List.of(new CloseException(request, responses));
        Acceptor acceptor = new TestAcceptor(connection);

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
            TestConnection connection = connections.next();
            connection.open();
            return connection;
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
        private List<String> responses;
        private boolean closed;

        TestConnection(String request, List<String> responses) {
            this.request = request;
            this.responses = responses;
            this.closed = false;
        }

        public Optional<String> receive() {
            return closed ? Optional.empty() : Optional.of(request);
        }

        public void send(Response response) {
            responses.add(new String(response.serialize()));
        }

        public void close() {
            closed = true;
        }

        void open() {
            closed = false;
        }
    }

    private class SendException extends TestConnection {
        SendException(String request, List<String> responses) {
            super(request, responses);
        }

        public void send(Response response) {
            throw new RuntimeException(ErrorMessages.SEND_TO_CONNECTION);
        }
    }

    private class CloseException extends TestConnection {
        CloseException(String request, List<String> responses) {
            super(request, responses);
        }

        public void close() {
            throw new RuntimeException(ErrorMessages.CLOSE_CONNECTION);
        }
    }
}
