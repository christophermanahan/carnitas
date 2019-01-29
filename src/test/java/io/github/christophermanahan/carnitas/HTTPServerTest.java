package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class HTTPServerTest {
    private TestParser parser;
    private TestHandler handler;
    private TestLogger logger;

    @BeforeEach
    void setup() {
        parser = new TestParser();
        handler = new TestHandler();
        logger = new TestLogger();
    }

    @Test
    void itWillServeGETRequests() {
        String request = "GET /simple_get";
        List<ReadableConnection> connections = List.of(new ReadableConnection(request + HTTPResponse.CRLF));
        Listener listener = new TestListener(connections);

        new HTTPServer(parser, handler, logger).start(listener, new Once());

        byte[] expectedResponse = new HTTPResponse(HTTPResponse.Status.OK).serialize();
        assertArrayEquals(expectedResponse, connections.get(0).response.serialize());
    }

    @Test
    void itWillServePOSTRequests() {
        String request = "POST simple_post";
        List<ReadableConnection> connections = List.of(new ReadableConnection(request + HTTPResponse.CRLF));
        Listener listener = new TestListener(connections);

        new HTTPServer(parser, handler, logger).start(listener, new Once());

        byte[] expectedResponse = new HTTPResponse(HTTPResponse.Status.CREATED).serialize();
        assertArrayEquals(expectedResponse, connections.get(0).response.serialize());
    }

    @Test
    void itWillServeRequestsBasedOnContext() {
        String request = "GET simple_get";
        List<ReadableConnection> connections = List.of(
          new ReadableConnection(request + HTTPResponse.CRLF),
          new ReadableConnection(request + HTTPResponse.CRLF)
        );
        Listener listener = new TestListener(connections);

        new HTTPServer(parser, handler, logger).start(listener, new Twice());

        byte[] expectedResponse = new HTTPResponse(HTTPResponse.Status.OK).serialize();
        assertArrayEquals(expectedResponse, connections.get(0).response.serialize());
        assertArrayEquals(expectedResponse, connections.get(1).response.serialize());
    }

    @Test
    void itWillLogAMessageIfListenFails() {
        String message = "Failed!";
        Listener listener = () -> {
            throw new RuntimeException(message);
        };

        new HTTPServer(parser, handler, logger).start(listener, new Once());

        assertEquals(message, logger.logged());
    }

    @Test
    void itWillLogAMessageIfSendFails() {
        String message = "Failed!";
        Connection connection = new Connection() {
            public void send(HTTPResponse response) {
                throw new RuntimeException(message);
            }

            public void send2(HTTPResponse2 response2) {
                throw new RuntimeException(message);
            }

            public void close() {
            }

            public Optional<Character> read() {
                return Optional.empty();
            }
        };
        Listener listener = () -> connection;
        Parser parser = reader -> Optional.of(new HTTPRequest(HTTPRequest.Method.GET, "/simple_get"));
        new HTTPServer(parser, handler, logger).start(listener, new Once());

        assertEquals(message, logger.logged());
    }

    @Test
    void itWillServeGETRequests2() {
        String request = "GET /simple_get";
        List<ReadableConnection> connections = List.of(new ReadableConnection(request + HTTPResponse.CRLF));
        Listener listener = new TestListener(connections);

        new HTTPServer(parser, handler, logger).start2(listener, new Once());

        HTTPResponse2 expectedResponse = new ResponseBuilder()
          .setStatus(HTTPResponse2.Status.OK)
          .get();
        assertTrue(expectedResponse.equals(connections.get(0).response2));
    }

    @Test
    void itWillServePOSTRequests2() {
        String request = "POST simple_post";
        List<ReadableConnection> connections = List.of(new ReadableConnection(request + HTTPResponse.CRLF));
        Listener listener = new TestListener(connections);

        new HTTPServer(parser, handler, logger).start2(listener, new Once());

        HTTPResponse2 expectedResponse = new ResponseBuilder()
          .setStatus(HTTPResponse2.Status.CREATED)
          .get();
        assertTrue(expectedResponse.equals(connections.get(0).response2));
    }

    @Test
    void itWillServeRequestsBasedOnContext2() {
        String request = "GET simple_get";
        List<ReadableConnection> connections = List.of(
          new ReadableConnection(request + HTTPResponse.CRLF),
          new ReadableConnection(request + HTTPResponse.CRLF)
        );
        Listener listener = new TestListener(connections);

        new HTTPServer(parser, handler, logger).start2(listener, new Twice());

        HTTPResponse2 expectedResponse = new ResponseBuilder()
          .setStatus(HTTPResponse2.Status.OK)
          .get();
        assertTrue(expectedResponse.equals(connections.get(0).response2));
        assertTrue(expectedResponse.equals(connections.get(1).response2));
    }

    @Test
    void itWillLogAMessageIfListenFails2() {
        String message = "Failed!";
        Listener listener = () -> {
            throw new RuntimeException(message);
        };

        new HTTPServer(parser, handler, logger).start2(listener, new Once());

        assertEquals(message, logger.logged());
    }

    @Test
    void itWillLogAMessageIfSendFails2() {
        String message = "Failed!";
        Connection connection = new Connection() {
            public void send(HTTPResponse response) {
                throw new RuntimeException(message);
            }

            public void send2(HTTPResponse2 response2) {
                throw new RuntimeException(message);
            }

            public void close() {
            }

            public Optional<Character> read() {
                return Optional.empty();
            }
        };
        Listener listener = () -> connection;
        Parser parser = reader -> Optional.of(new HTTPRequest(HTTPRequest.Method.GET, "/simple_get"));
        new HTTPServer(parser, handler, logger).start2(listener, new Once());

        assertEquals(message, logger.logged());
    }

    private class TestListener implements Listener {
        private final Iterator<ReadableConnection> connections;

        TestListener(List<ReadableConnection> connections) {
            this.connections = connections.iterator();
        }

        public Connection listen() {
            return connections.next();
        }
    }

    private class ReadableConnection implements Connection {
        private final Iterator<String> request;
        HTTPResponse response;
        private HTTPResponse2 response2;

        ReadableConnection(String request) {
            this.request = List.of(request.split("")).iterator();
        }

        public Optional<Character> read() {
            return Optional.of(request.next().charAt(0));
        }

        public void send(HTTPResponse response) {
            this.response = response;
        }

        public void send2(HTTPResponse2 response) {
            this.response2 = response;
        }

        public void close() {
        }
    }

    private class Once implements Consumer<Runnable> {
        public void accept(Runnable runnable) {
            runnable.run();
        }
    }

    private class Twice implements Consumer<Runnable> {
        public void accept(Runnable runnable) {
            runnable.run();
            runnable.run();
        }
    }

    private class TestParser implements Parser {
        public Optional<HTTPRequest> parse(Reader reader) {
            HTTPRequest.Method method = HTTPRequest.Method.valueOf(reader.readUntil(" ").get());
            String uri = reader.readUntil(HTTPResponse.CRLF).get();
            return Optional.of(new HTTPRequest(method, uri));
        }
    }

    private class TestHandler implements Handler {
        public HTTPResponse handle(HTTPRequest request) {
            HTTPResponse.Status code = request.method().equals(HTTPRequest.Method.GET) ? HTTPResponse.Status.OK : HTTPResponse.Status.CREATED;
            return new HTTPResponse(code);
        }

        public HTTPResponse2 handle2(HTTPRequest request) {
            HTTPResponse2.Status code = request.method().equals(HTTPRequest.Method.GET) ? HTTPResponse2.Status.OK : HTTPResponse2.Status.CREATED;
            return new ResponseBuilder()
              .setStatus(code)
              .get();
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