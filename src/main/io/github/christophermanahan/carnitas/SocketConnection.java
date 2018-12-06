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
      throw new CloseSocketFailed(e);
    }
  }

  static class CloseSocketFailed extends RuntimeException {
    private static IOException exception;

    CloseSocketFailed(IOException e) {
      this.exception = e;
    }

    public static String dueTo() {
      return "Socket failed to close due to: " + exception;
    }
  }
}
