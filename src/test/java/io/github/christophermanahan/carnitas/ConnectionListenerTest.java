package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConnectionListenerTest {
    @Test
    void listensForAConnection() throws IOException {
        String request = "GET http://localhost:80/simple_get HTTP/1.1";
        ServerSocket serverSocket = new TestServerSocket(request);

        Connection connection = new ConnectionListener(serverSocket).listen();

        assertEquals(request, connection.receiver().receiveLine());
    }

    @Test
    void throwsExceptionIfConnectionAcceptionFails() throws IOException {
        ServerSocket serverSocket = new ConnectionException();

        Listener listener = new ConnectionListener(serverSocket);
        RuntimeException e = assertThrows(RuntimeException.class, listener::listen);

        Assertions.assertEquals(ErrorMessages.ACCEPT_CONNECTION, e.getMessage());
    }

    @Test
    void closesServerSocket() throws IOException {
        boolean closed = false;
        ServerSocket serverSocket = new ClosableServerSocket(closed);

        Listener listener = new ConnectionListener(serverSocket);
        listener.close();

        Assertions.assertFalse(listener.isListening());
    }

    @Test
    void checksIfSocketIsOpen() throws IOException {
        boolean closed = false;
        ServerSocket serverSocket = new ClosableServerSocket(closed);

        Listener listener = new ConnectionListener(serverSocket);

        Assertions.assertTrue(listener.isListening());
    }

    @Test
    void checkIfSocketIsClosed() throws IOException {
        boolean closed = true;
        ServerSocket serverSocket = new ClosableServerSocket(closed);

        Listener listener = new ConnectionListener(serverSocket);

        Assertions.assertFalse(listener.isListening());
    }

    private class TestServerSocket extends ServerSocket {
        private final String request;

        TestServerSocket(String request) throws IOException {
            this.request = request;
        }

        public Socket accept() {
            return new TestSocket(request);
        }
    }

    private class ConnectionException extends ServerSocket {
        ConnectionException() throws IOException {
        }

        public Socket accept() throws IOException {
            throw new IOException();
        }
    }

    private class TestSocket extends Socket {
        private final String request;

        TestSocket(String request) {
            this.request = request;
        }

        public InputStream getInputStream() {
            return new ByteArrayInputStream(request.getBytes());
        }
    }

    private class ClosableServerSocket extends ServerSocket {
        private boolean closed;

        ClosableServerSocket(boolean closed) throws IOException {
            this.closed = closed;
        }

        public boolean isClosed() {
            return closed;
        }

        public void close() {
            closed = true;
        }
    }
}
