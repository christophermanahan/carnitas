package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConnectionListenerTest {
    @Test
    void listensForAConnection() throws IOException {
        Response response = new ResponseBuilder()
          .set(Response.Status.OK)
          .get();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ServerSocket serverSocket = new TestServerSocket(output);

        Connection connection = new ConnectionListener(serverSocket).listen();

        connection.send(response);
        assertEquals(new String(new Serializer().serialize(response)), new String(output.toByteArray()));
    }

    @Test
    void throwsExceptionIfConnectionAcceptationFails() throws IOException {
        ServerSocket serverSocket = new ConnectionException();

        Listener listener = new ConnectionListener(serverSocket);
        RuntimeException e = assertThrows(RuntimeException.class, listener::listen);

        assertEquals(ErrorMessages.ACCEPT_CONNECTION, e.getMessage());
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

        public OutputStream getOutputStream() {
            return output;
        }
    }
}
