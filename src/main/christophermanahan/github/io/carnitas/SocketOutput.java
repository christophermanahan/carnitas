package christophermanahan.github.io.carnitas;

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
      throw new IllegalStateException("Could not write to socket output", e);
    }
  }
}
