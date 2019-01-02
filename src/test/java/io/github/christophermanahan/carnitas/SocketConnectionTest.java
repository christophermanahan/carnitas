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
    void receivesDataFromSocket() {
        String request = "GET http://localhost:80/simple_get HTTP/1.1";
        Socket socket = new TestSocket(request);

        Optional<String> received = new SocketConnection(socket).receive();

        assertEquals(Optional.of(request), received);
    }

    @Test
    void sendsResponseBytesToSocket() throws IOException {
        String request = "GET http://localhost:80/simple_get HTTP/1.1";
        Socket socket = new TestSocket(null);

        new SocketConnection(socket).send(new TestResponse(request));

        assertEquals(request, socket.getOutputStream().toString());
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

    @Test
    void checksOpenSocketStatus() {
        Socket socket = new TestSocket(null);

        Connection connection = new SocketConnection(socket);

        assertFalse(socket.isClosed());
        assertTrue(connection.isOpen());
    }

    private class TestSocket extends Socket {

        private final String receive;
        private OutputStream output;
        private boolean closed;

        public TestSocket(String receive) {
            this.receive = receive;
            this.output = new ByteArrayOutputStream();
            this.closed = false;
        }

        public InputStream getInputStream() {
            return new ByteArrayInputStream(receive.getBytes());
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

    private class TestResponse implements Response {

        private final String request;

        TestResponse(String request) {
            this.request = request;
        }

        public byte[] serialize() {
            return request.getBytes();
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
