package io.github.christophermanahan.carnitas;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

public class SocketConnection implements Connection, Readable {
    private final Socket socket;

    SocketConnection(Socket socket) {
        this.socket = socket;
    }

    public Optional<Character> read() {
        try {
            return Optional.of((char) socket.getInputStream().read());
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public void send(HTTPResponse response) {
        try {
            socket.getOutputStream().write(new Serializer().serialize(response));
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessages.SEND_TO_CONNECTION);
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessages.CLOSE_CONNECTION);
        }
    }
}
