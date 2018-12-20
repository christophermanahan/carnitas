package io.github.christophermanahan.carnitas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Optional;

public class SocketConnection implements Connection {

    private final Socket socket;

    SocketConnection(Socket socket) {
        this.socket = socket;
    }

    public Optional<String> receive() {
        try {
            BufferedReader receiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return Optional.ofNullable(receiver.readLine());
        } catch (IOException e) {
            return Optional.empty();
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
}
