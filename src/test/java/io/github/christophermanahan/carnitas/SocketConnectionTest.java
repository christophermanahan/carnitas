package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SocketConnectionTest {
    @Test
    void itReadsACharacter() {
        String request = "GET";
        Socket socket = new TestSocket(request);

        Optional<Character> read = new SocketConnection(socket).read();

        assertEquals(Optional.of(request.charAt(0)), read);
    }

    @Test
    void itReadsInTheSocketInput() {
        String body = "name=<something>";
        String request = HTTPRequest.Method.POST + "/simple_post " + HTTPResponse.VERSION + Serializer.CRLF
          + Headers.contentLength(body.length())
          + Serializer.BLANK_LINE
          + body;
        Socket socket = new TestSocket(request);

        Optional<String> read = new SocketConnection(socket).readAll().map(String::new);

        assertEquals(Optional.of(request), read);
    }

    @Test
    void itIsEmptyIfSocketInputFails() {
        class ReadStreamException extends Socket {
            public InputStream getInputStream() throws IOException {
                throw new IOException();
            }
        }

        Optional read = new SocketConnection(new ReadStreamException()).readAll();

        assertEquals(Optional.empty(), read);
    }

    @Test
    void itSendsResponseBytesToTheSocket() throws IOException {
        HTTPResponse response = new ResponseBuilder()
          .setStatus(HTTPResponse.Status.OK)
          .get();
        Socket socket = new TestSocket(null);

        new SocketConnection(socket).send(response);

        assertEquals(new String(new Serializer().serialize(response)), socket.getOutputStream().toString());
    }

    @Test
    void itThrowsAnExceptionIfSendFails() {
        class OutputStreamException extends Socket {
            public OutputStream getOutputStream() throws IOException {
                throw new IOException();
            }
        }
        Connection connection = new SocketConnection(new OutputStreamException());

        RuntimeException e = assertThrows(RuntimeException.class, ()->{ connection.send(null); });

        assertEquals(ErrorMessages.SEND_TO_CONNECTION, e.getMessage());
    }

    @Test
    void itClosesTheSocketConnection() {
        Socket socket = new TestSocket(null);

        new SocketConnection(socket).close();

        assertTrue(socket.isClosed());
    }

    @Test
    void itThrowsAnExceptionIfCloseFails() {
        class CloseException extends Socket {
            public synchronized void close() throws IOException {
                throw new IOException();
            }
        }
        Connection connection = new SocketConnection(new CloseException());

        RuntimeException e = assertThrows(RuntimeException.class, connection::close);

        assertEquals(ErrorMessages.CLOSE_CONNECTION, e.getMessage());
    }

    private class TestSocket extends Socket {
        private final String request;
        private OutputStream output;
        private boolean closed;

        TestSocket(String request) {
            this.request = request;
            this.output = new ByteArrayOutputStream();
            this.closed = false;
        }

        public InputStream getInputStream() {
            return new ByteArrayInputStream(request.getBytes());
        }

        public OutputStream getOutputStream() {
            return output;
        }

        public void close() {
            closed = true;
        }

        public boolean isClosed() {
            return closed;
        }
    }
}
