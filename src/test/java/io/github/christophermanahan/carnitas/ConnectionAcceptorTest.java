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

class ConnectionAcceptorTest {
    @Test
    void listensForAConnection() throws IOException {
        String request = "GET http://localhost:80/simple_get HTTP/1.1";
        ServerSocket serverSocket = new TestServerSocket(request);

        Connection connection = new ConnectionAcceptor(serverSocket).accept();

        assertEquals(request, connection.receiver().receiveLine());
    }

    @Test
    void throwsExceptionIfConnectionAcceptionFails() throws IOException {
        ServerSocket serverSocket = new ConnectionException();

        Acceptor acceptor = new ConnectionAcceptor(serverSocket);
        RuntimeException e = assertThrows(RuntimeException.class, acceptor::accept);

        Assertions.assertEquals(ErrorMessages.ACCEPT_CONNECTION, e.getMessage());
    }

    @Test
    void closesServerSocket() throws IOException {
        boolean closed = false;
        ServerSocket serverSocket = new ClosableServerSocket(closed);

        Acceptor acceptor = new ConnectionAcceptor(serverSocket);
        acceptor.close();

        Assertions.assertFalse(acceptor.isAccepting());
    }

    @Test
    void checksIfSocketIsOpen() throws IOException {
        boolean closed = false;
        ServerSocket serverSocket = new ClosableServerSocket(closed);

        Acceptor acceptor = new ConnectionAcceptor(serverSocket);

        Assertions.assertTrue(acceptor.isAccepting());
    }

    @Test
    void checkIfSocketIsClosed() throws IOException {
        boolean closed = true;
        ServerSocket serverSocket = new ClosableServerSocket(closed);

        Acceptor acceptor = new ConnectionAcceptor(serverSocket);

        Assertions.assertFalse(acceptor.isAccepting());
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
