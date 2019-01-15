package io.github.christophermanahan.carnitas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketConnection implements Connection, Readable {
    private final Socket socket;

    SocketConnection(Socket socket) {
        this.socket = socket;
    }

    public Receiver receiver() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return new RequestReceiver(reader);
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessages.OPEN_INPUT_STREAM);
        }
    }

    public char read() {
        return 0;
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
