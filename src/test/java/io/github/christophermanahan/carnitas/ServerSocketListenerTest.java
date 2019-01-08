package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ServerSocketListenerTest {
    @Test
    void listensForAConnection() throws IOException {
        String request = "GET http://localhost:80/simple_get HTTP/1.1";
        ServerSocket serverSocket = new TestServerSocket(request);

        Connection connection = new ServerSocketListener(serverSocket).listen();

        assertEquals(Optional.of(request), connection.receive());
    }

    @Test
    void throwsExceptionIfConnectionAcceptionFails() throws IOException {
        ServerSocket serverSocket = new ConnectionException();
        Listener listener = new ServerSocketListener(serverSocket);

        RuntimeException e = assertThrows(RuntimeException.class, listener::listen);

        Assertions.assertEquals(ErrorMessages.ACCEPT_CONNECTION, e.getMessage());
    }

    private class TestServerSocket extends ServerSocket {
        private final String request;

        public TestServerSocket(String request) throws IOException {
            this.request = request;
        }

        public Socket accept() {
            return new TestSocket(request);
        }
    }

    private class ConnectionException extends ServerSocket {
        public ConnectionException() throws IOException {
        }

        public Socket accept() throws IOException {
            throw new IOException();
        }
    }

    private class TestSocket extends Socket {
        private final String request;

        public TestSocket(String request) {
            this.request = request;
        }

        public InputStream getInputStream() {
            return new ByteArrayInputStream(request.getBytes());
        }
    }
}
