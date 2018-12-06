package christophermanahan.github.io.carnitas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Optional;

public class SocketInput implements Input {

  private final Socket socket;

  SocketInput(Socket socket) {
    this.socket = socket;
  }

  public Optional<String> receive() {
    try {
      return Optional.ofNullable(reader().readLine());
    } catch (IOException e) {
      return Optional.empty();
    }
  }

  private BufferedReader reader() {
    try {
      return new BufferedReader(
        new InputStreamReader(socket.getInputStream())
      );
    } catch (IOException e) {
      throw new IllegalStateException("Could not get input stream from socket", e);
    }
  }
}
