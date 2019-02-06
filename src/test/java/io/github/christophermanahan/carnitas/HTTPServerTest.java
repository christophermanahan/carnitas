package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        List<ReadableConnection> connections = List.of(new ReadableConnection(request + Serializer.CRLF));
        Listener listener = new TestListener(connections);

        new HTTPServer(parser, handler, logger).start(listener, new Once());

        Response expectedResponse = new ResponseBuilder()
          .set(Response.Status.OK)
          .get();
        assertEquals(expectedResponse, connections.get(0).response);
    }

    @Test
    void itWillServePOSTRequests() {
        String request = "POST simple_post";
        List<ReadableConnection> connections = List.of(new ReadableConnection(request + Serializer.CRLF));
        Listener listener = new TestListener(connections);

        new HTTPServer(parser, handler, logger).start(listener, new Once());

        Response expectedResponse = new ResponseBuilder()
          .set(Response.Status.CREATED)
          .get();
        assertEquals(expectedResponse, connections.get(0).response);
    }

    @Test
    void itWillServeRequestsBasedOnContext() {
        String request = "GET simple_get";
        List<ReadableConnection> connections = List.of(
          new ReadableConnection(request + Serializer.CRLF),
          new ReadableConnection(request + Serializer.CRLF)
        );
        Listener listener = new TestListener(connections);

        new HTTPServer(parser, handler, logger).start(listener, new Twice());

        Response expectedResponse = new ResponseBuilder()
          .set(Response.Status.OK)
          .get();
        assertEquals(expectedResponse, connections.get(0).response);
        assertEquals(expectedResponse, connections.get(1).response);
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
            public void send(Response response) {
                throw new RuntimeException(message);
            }

            public void close() {
            }

            public Optional<Character> read() {
                return Optional.empty();
            }
        };
        Listener listener = () -> connection;
        Parser parser = reader -> Optional.of(new Request(Request.Method.GET, "/simple_get"));
        new HTTPServer(parser, handler, logger).start(listener, new Once());

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
        private Response response;

        ReadableConnection(String request) {
            this.request = List.of(request.split("")).iterator();
        }

        public Optional<Character> read() {
            return Optional.of(request.next().charAt(0));
        }

        public void send(Response response) {
            this.response = response;
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
        public Optional<Request> parse(Reader reader) {
            Request.Method method = Request.Method.valueOf(reader.readUntil(" ").get());
            String uri = reader.readUntil(Serializer.CRLF).get();
            return Optional.of(new Request(method, uri));
        }
    }

    private class TestHandler implements Handler {
        public Response handle(Request request) {
            Response.Status code = request.method().equals(Request.Method.GET) ? Response.Status.OK : Response.Status.CREATED;
            return new ResponseBuilder()
              .set(code)
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