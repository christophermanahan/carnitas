package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HTTPServerTest {

    private String simpleGETRequest;
    private String simplePOSTRequest;
    private List<String> received;
    private List<String> sent;
    private Connection connection;
    private Listener listener;
    private TestLogger logger;
    private Parser parser;

    @BeforeEach
    void setup() {
        sent = new ArrayList<>();
        logger = new TestLogger();
    }

    @Test
    void sendsHTTP200ResponsesWhileReceivingData() {
        simpleGETRequest = "GET http://localhost:80/simple_get HTTP/1.1";
        received = List.of(simpleGETRequest, simpleGETRequest, simpleGETRequest);
        connection = new TestConnection(received, sent);
        listener = new TestListener(connection);
        parser = new GetParser();

        new HTTPServer(listener, parser, logger).run();

        String response = new String(new HTTPResponse("").serialize());
        List<String> responses = List.of(response, response, response);
        assertEquals(responses, sent);
    }

    @Test
    void sendsHTTP201ResponseWhenReceivingPOST() {
        String body = "hello world";
        simplePOSTRequest = "POST http://localhost:80/simple_get HTTP/1.1";
        received = List.of(simplePOSTRequest);
        connection = new TestConnection(received, sent);
        listener = new TestListener(connection);
        parser = new PostParser(body);

        new HTTPServer(listener, parser, logger).run();

        List<String> response = List.of(new String(new HTTPResponse(body).serialize()));
        assertEquals(response, sent);
    }

    @Test
    void connectionIsClosedWhenClientDisconnects() {
        simpleGETRequest = "GET http://localhost:80/simple_get HTTP/1.1";
        received = List.of(simpleGETRequest, simpleGETRequest, simpleGETRequest);
        connection = new TestConnection(received, sent);
        listener = new TestListener(connection);
        parser = new GetParser();

        new HTTPServer(listener, parser, logger).run();

        assertEquals(Optional.empty(), parser.parse(connection.receiver()));
    }

    @Test
    void logsExceptionIfListenFails() {
        simpleGETRequest = "GET http://localhost:80/simple_get HTTP/1.1";
        received = List.of(simpleGETRequest, simpleGETRequest, simpleGETRequest);
        connection = new TestConnection(received, sent);
        listener = new ListenException(connection);
        parser = new GetParser();

        new HTTPServer(listener, parser, logger).run();

        assertEquals(ErrorMessages.ACCEPT_CONNECTION, logger.log());
    }

    @Test
    void logsExceptionIfSendFails() {
        simpleGETRequest = "GET http://localhost:80/simple_get HTTP/1.1";
        received = List.of(simpleGETRequest, simpleGETRequest, simpleGETRequest);
        connection = new SendException(received, sent);
        listener = new TestListener(connection);
        parser = new GetParser();

        new HTTPServer(listener, parser, logger).run();

        assertEquals(ErrorMessages.SEND_TO_CONNECTION, logger.log());
    }

    @Test
    void logsExceptionIfCloseFails() {
        simpleGETRequest = "GET http://localhost:80/simple_get HTTP/1.1";
        received = List.of(simpleGETRequest, simpleGETRequest, simpleGETRequest);
        connection = new CloseException(received, sent);
        listener = new TestListener(connection);
        parser = new GetParser();

        new HTTPServer(listener, parser, logger).run();

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

        public Receiver receiver() {
            return closed || !received.hasNext() ? new ClosedReceiver() : new OpenReceiver(received.next());
        }

        public void send(Response response) {
            sent.add(new String(response.serialize()));
        }

        public void close() {
            closed = true;
        }

        public boolean isOpen() {
            return !closed;
        }
    }

    private class OpenReceiver implements Receiver {

        private final String receive;

        public OpenReceiver(String receive) {
           this.receive = receive;
        }

        public String receiveLine() {
            return receive;
        }
    }

    private class ClosedReceiver implements Receiver {

        public String receiveLine() {
            return "";
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

    private class GetParser implements Parser {

        public Optional<String> parse(Receiver receiver) {
            return receiver.receiveLine().isEmpty() ? Optional.empty() : Optional.of("");
        }
    }

    private class PostParser implements Parser {

        private final String body;

        PostParser(String body) {
            this.body = body;
        }

        public Optional<String> parse(Receiver receiver) {
            return receiver.receiveLine().isEmpty() ? Optional.empty() : Optional.of(body);
        }
    }
}
