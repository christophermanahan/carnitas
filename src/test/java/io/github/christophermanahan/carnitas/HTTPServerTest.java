package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
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
        String request = "GET";
        List<ReadableConnection> connections = List.of(new ReadableConnection(request + Constants.CRLF));
        Listener listener = new TestListener(connections);

        new HTTPServer(parser, handler, logger).start(listener, new Once());

        byte[] expectedResponse = new HTTPResponse(request).serialize();
        assertArrayEquals(expectedResponse, connections.get(0).response.serialize());
    }

    @Test
    void itWillServePOSTRequests() {
        String request = "POST";
        List<ReadableConnection> connections = List.of(new ReadableConnection(request + Constants.CRLF));
        Listener listener = new TestListener(connections);

        new HTTPServer(parser, handler, logger).start(listener, new Once());

        byte[] expectedResponse = new HTTPResponse(request).serialize();
        assertArrayEquals(expectedResponse, connections.get(0).response.serialize());
    }

    @Test
    void itWillServeRequestsBasedOnContext() {
        String request = "GET";
        List<ReadableConnection> connections = List.of(
          new ReadableConnection(request + Constants.CRLF),
          new ReadableConnection(request + Constants.CRLF)
        );
        Listener listener = new TestListener(connections);

        new HTTPServer(parser, handler, logger).start(listener, new Twice());

        byte[] expectedResponse = new HTTPResponse(request).serialize();
        assertArrayEquals(expectedResponse, connections.get(0).response.serialize());
        assertArrayEquals(expectedResponse, connections.get(1).response.serialize());
    }

    @Test
    void itWillLogAMessageIfListenFails() {
        String message = "Failed!";
        Listener listener = new Listener() {
            public Connection listen() {
                throw new RuntimeException(message);
            }

            public boolean isListening() {
                return false;
            }

            public void close() {
            }
        };

        new HTTPServer(parser, handler, logger).start(listener, new Once());

        assertEquals(message, logger.logged());
    }

    @Test
    void itWillLogAMessageIfSendFails() {
        String message = "Failed!";
        Connection connection = new Connection() {
            private int index = -1;

            public void send(HTTPResponse response) {
                throw new RuntimeException(message);
            }

            public void close() {
            }

            public Optional<Character> read() {
                index++;
                return Optional.of(
                  List.of(Constants.CRLF.split("")).get(index).charAt(0)
                );
            }
        };
        Listener listener = new Listener() {
            public Connection listen() {
                return connection;
            }

            public boolean isListening() {
                return false;
            }

            public void close() {
            }
        };

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

        public boolean isListening() {
            return false;
        }

        public void close() {

        }
    }

    private class ReadableConnection implements Connection {
        private final String request;
        private int index;
        HTTPResponse response;

        ReadableConnection(String request) {
            this.request = request;
            this.index = -1;
        }

        public Optional<Character> read() {
            index++;
            return Optional.of(request.charAt(index));
        }

        public void send(HTTPResponse response) {
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
        public Optional<HTTPRequest> parse(Reader reader) {
            return reader.readUntil(Constants.CRLF)
              .map(HTTPRequest::new);
        }
    }

    private class TestHandler implements Handler {
        public HTTPResponse handle(HTTPRequest request) {
            return new HTTPResponse(request.method());
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