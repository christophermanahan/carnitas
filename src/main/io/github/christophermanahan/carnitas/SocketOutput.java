package io.github.christophermanahan.carnitas;

import java.io.IOException;
import java.net.Socket;

public class SocketOutput implements Output {

  final Socket socket;

  public SocketOutput(Socket socket) {
    this.socket = socket;
  }

  public void send(String data) {
    try {
      socket.getOutputStream().write(data.concat("\n").getBytes());
    } catch (IOException e) {
      throw new SendToSocketFailed(e);
    }
  }

  static class SendToSocketFailed extends RuntimeException {
    private static IOException exception;

    SendToSocketFailed(IOException e) {
      this.exception = e;
    }

    public static String dueTo() {
      return "Sending to socket failed due to: " + exception;
    }
  }
}
