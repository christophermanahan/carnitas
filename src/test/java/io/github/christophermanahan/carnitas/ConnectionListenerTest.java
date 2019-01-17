package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionListenerTest {
    @Test
    void listensForAConnection() throws IOException {
        HTTPResponse response = new HTTPResponse("GET");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ServerSocket serverSocket = new TestServerSocket(output);

        Connection connection = new ConnectionListener(serverSocket).listen();

        connection.send(response);
        assertArrayEquals(response.serialize(), output.toByteArray());
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
        private final ByteArrayOutputStream output;

        TestServerSocket(ByteArrayOutputStream output) throws IOException {
            this.output = output;
        }

        public Socket accept() {
            return new TestSocket(output);
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
        private final ByteArrayOutputStream output;

        TestSocket(ByteArrayOutputStream output) {
            this.output = output;
        }

        public OutputStream getOutputStream() throws IOException {
            return output;
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
