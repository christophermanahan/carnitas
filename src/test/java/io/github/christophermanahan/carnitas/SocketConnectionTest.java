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
    void readsACharacter() {
        String request = "GET";
        Socket socket = new TestSocket(request);

        Optional<Character> read = new SocketConnection(socket).read();

        assertEquals(Optional.of(request.charAt(0)), read);
    }

    @Test
    void sendsResponseBytesToSocket() throws IOException {
        HTTPResponse response = new HTTPResponse(HTTPResponse.Status.OK);
        Socket socket = new TestSocket(null);

        new SocketConnection(socket).send(response);

        assertEquals(new String(response.serialize()), socket.getOutputStream().toString());
    }

    @Test
    void throwsExceptionIfSendFails() {
        Socket socket = new OutputStreamException();
        Connection connection = new SocketConnection(socket);

        RuntimeException e = assertThrows(RuntimeException.class, ()->{ connection.send(null); });

        assertEquals(ErrorMessages.SEND_TO_CONNECTION, e.getMessage());
    }

    @Test
    void closeClosesSocketConnection() {
        Socket socket = new TestSocket(null);

        new SocketConnection(socket).close();

        assertTrue(socket.isClosed());
    }

    @Test
    void throwsExceptionIfCloseFails() {
        Socket socket = new CloseException();
        Connection connection = new SocketConnection(socket);

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

    private class OutputStreamException extends Socket {
        public OutputStream getOutputStream() throws IOException {
            throw new IOException();
        }
    }

    private class CloseException extends Socket {
        public synchronized void close() throws IOException {
            throw new IOException();
        }
    }
}
