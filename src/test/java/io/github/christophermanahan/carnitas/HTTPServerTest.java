package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HTTPServerTest {

    private String request;
    private List<String> received;
    private List<String> sent;
    private Connection connection;
    private Listener listener;
    private TestLogger logger;

    @BeforeEach
    void setup() {
        sent = new ArrayList<>();
        request = "GET simple_get HTTP/1.1";
        logger = new TestLogger();
    }

    @Test
    void sendsHTTPResponsesWhileReceivingData() {
        received = List.of(request, request, request);
        connection = new TestConnection(received, sent);
        listener = new TestListener(connection);

        new HTTPServer(listener, logger).run();

        String response = new String(new HTTPResponse().serialize());
        List<String> responses = List.of(response, response, response);
        assertEquals(responses, sent);
    }

    @Test
    void connectionIsClosedWhenClientDisconnects() {
        received = List.of(request, request, request);
        connection = new TestConnection(received, sent);
        listener = new TestListener(connection);

        new HTTPServer(listener, logger).run();

        assertEquals(Optional.empty(), connection.receive());
    }

    @Test
    void logsExceptionIfListenFails() {
        received = List.of(request, request, request);
        connection = new TestConnection(received, sent);
        listener = new ListenException(connection);

        new HTTPServer(listener, logger).run();

        assertEquals(ErrorMessages.ACCEPT_CONNECTION, logger.log());
    }

    @Test
    void logsExceptionIfSendFails() {
        received = List.of(request, request, request);
        connection = new SendException(received, sent);
        listener = new TestListener(connection);

        new HTTPServer(listener, logger).run();

        assertEquals(ErrorMessages.SEND_TO_CONNECTION, logger.log());
    }

    @Test
    void logsExceptionIfCloseFails() {
        received = List.of(request, request, request);
        connection = new CloseException(received, sent);
        listener = new TestListener(connection);

        new HTTPServer(listener, logger).run();

        assertEquals(ErrorMessages.CLOSE_CONNECTION, logger.log());
    }

    private class TestLogger implements Logger {

        private final StringBuilder log;

        TestLogger() {
            this.log = new StringBuilder();
        }

        public String log() {
            return log.toString();
        }

        public void log(String message) {
            log.append(message);
        }
    }

    private class TestListener implements Listener {

        private final Connection connection;

        public TestListener(Connection connection) {
            this.connection = connection;
        }

        public Connection listen() {
            return connection;
        }
    }

    private class ListenException extends TestListener {

        public ListenException(Connection connection) {
            super(connection);
        }

        public Connection listen() {
            throw new RuntimeException(ErrorMessages.ACCEPT_CONNECTION);
        }
    }

    private class TestConnection implements Connection {

        private Iterator<String> received;
        private List<String> sent;
        private boolean closed;

        public TestConnection(List<String> received, List<String> sent) {
            this.received = received.iterator();
            this.sent = sent;
            this.closed = false;
        }

        public Optional<String> receive() {
            return closed || !received.hasNext() ? Optional.empty() : Optional.of(received.next());
        }

        public void send(Response response) {
            sent.add(new String(response.serialize()));
        }

        public void close() {
            closed = true;
        }
    }

    private class SendException extends TestConnection {

        public SendException(List<String> received, List<String> sent) {
            super(received, sent);
        }

        public void send(Response response) {
            throw new RuntimeException(ErrorMessages.SEND_TO_CONNECTION);
        }
    }

    private class CloseException extends TestConnection {

        public CloseException(List<String> received, List<String> sent) {
            super(received, sent);
        }

        public void close() {
            throw new RuntimeException(ErrorMessages.CLOSE_CONNECTION);
        }
    }
}
