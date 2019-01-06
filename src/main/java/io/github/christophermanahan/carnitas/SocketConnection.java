package io.github.christophermanahan.carnitas;

import java.io.IOException;
import java.net.Socket;

public class SocketConnection implements Connection {

    private final Socket socket;

    SocketConnection(Socket socket) {
        this.socket = socket;
    }

    public Receiver receiver() {
        try {
            return new ConnectionReceiver(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessages.OPEN_INPUT_STREAM);
        }
    }

    public void send(Response response) {
        try {
            socket.getOutputStream().write(response.serialize());
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

    public boolean isOpen() {
        return !socket.isClosed();
    }
}
