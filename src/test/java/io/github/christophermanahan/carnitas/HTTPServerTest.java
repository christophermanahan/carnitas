package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HTTPServerTest {
    private List<String> sent;
    private TestLogger logger;

    @BeforeEach
    void setup() {
        sent = new ArrayList<>();
        logger = new TestLogger();
    }

    @Test
    void sendsHTTPResponsesWhenTestConnectionsSendRequests() {
        String received = "GET /simple_get HTTP/1.1";
        int receiveCount = 3;
        TestConnection connection = new TestConnection(received, sent);
        Acceptor acceptor = new TestAcceptor(connection, receiveCount);

        new HTTPServer(acceptor, logger).run();

        String response = new String(new HTTPResponse().serialize());
        List<String> responses = List.of(response, response, response);
        assertEquals(responses, sent);
    }

    @Test
    void connectionIsClosedAfterResponse() {
        String received = "GET /simple_get HTTP/1.1";
        int receiveCount = 1;
        TestConnection connection = new TestConnection(received, sent);
        Acceptor acceptor = new TestAcceptor(connection, receiveCount);

        new HTTPServer(acceptor, logger).run();

        assertEquals(Optional.empty(), connection.receive());
    }

    @Test
    void logsExceptionIfAcceptFails() {
        String received = "GET /simple_get HTTP/1.1";
        int receiveCount = 1;
        TestConnection connection = new TestConnection(received, sent);
        Acceptor acceptor = new AcceptorException(connection, receiveCount);

        new HTTPServer(acceptor, logger).run();

        assertEquals(ErrorMessages.ACCEPT_CONNECTION, logger.logged());
    }

    @Test
    void logsExceptionIfSendFails() {
        String received = "GET /simple_get HTTP/1.1";
        int receiveCount = 1;
        TestConnection connection = new SendException(received, sent);
        Acceptor acceptor = new TestAcceptor(connection, receiveCount);

        new HTTPServer(acceptor, logger).run();

        assertEquals(ErrorMessages.SEND_TO_CONNECTION, logger.logged());
    }

    @Test
    void logsExceptionIfCloseFails() {
        String received = "GET /simple_get HTTP/1.1";
        int receiveCount = 1;
        TestConnection connection = new CloseException(received, sent);
        Acceptor acceptor = new TestAcceptor(connection, receiveCount);

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
        private final TestConnection connection;
        int receiveCount;

        TestAcceptor(TestConnection connection, int receiveCount) {
            this.connection = connection;
            this.receiveCount = receiveCount;
        }

        public TestConnection accept() {
            connection.open();
            receiveCount -= 1;
            return connection;
        }

        public void close() {
        }

        public boolean isAccepting() {
            return receiveCount != 0;
        }
    }

    private class AcceptorException extends TestAcceptor {
        AcceptorException(TestConnection connection, int receiveCount) {
            super(connection, receiveCount);
        }

        public TestConnection accept() {
            receiveCount -= 1;
            throw new RuntimeException(ErrorMessages.ACCEPT_CONNECTION);
        }
    }

    private class TestConnection implements Connection {
        private final String received;
        private List<String> sent;
        private boolean closed;

        TestConnection(String received, List<String> sent) {
            this.received = received;
            this.sent = sent;
            this.closed = false;
        }

        public Optional<String> receive() {
            return closed ? Optional.empty() : Optional.of(received);
        }

        public void send(Response response) {
            sent.add(new String(response.serialize()));
        }

        public void close() {
            closed = true;
        }

        void open() {
            closed = false;
        }
    }

    private class SendException extends TestConnection {
        SendException(String received, List<String> sent) {
            super(received, sent);
        }

        public void send(Response response) {
            throw new RuntimeException(ErrorMessages.SEND_TO_CONNECTION);
        }
    }

    private class CloseException extends TestConnection {
        CloseException(String received, List<String> sent) {
            super(received, sent);
        }

        public void close() {
            throw new RuntimeException(ErrorMessages.CLOSE_CONNECTION);
        }
    }
}
