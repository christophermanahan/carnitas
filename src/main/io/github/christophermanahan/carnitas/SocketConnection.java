package io.github.christophermanahan.carnitas;

import java.io.IOException;
import java.net.Socket;

public class SocketConnection implements Connection {

  final Socket socket;

  SocketConnection(Socket socket) {
    this.socket = socket;
  }

  public boolean isOpen() {
    return !socket.isClosed();
  }

  public void close() {
    try {
      socket.close();
    } catch (IOException e) {
      throw new IllegalStateException("Cannot close socket", e);
    }
  }
}
