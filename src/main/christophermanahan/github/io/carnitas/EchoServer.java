package christophermanahan.github.io.carnitas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer implements Server {

  final ServerSocket serverSocket;

  EchoServer(ServerSocket serverSocket) {
    this.serverSocket = serverSocket;
  }

  public void run() {
    try {
      Socket socket = serverSocket.accept();
      while(!socket.isClosed()) {
        String incomingData = new BufferedReader(
          new InputStreamReader(socket.getInputStream())
        ).readLine();

        if (incomingData == null) {
          socket.close();
        } else {
          socket.getOutputStream().write(incomingData.concat("\n").getBytes());
        }
      }
    } catch (IOException e) {
      throw new IllegalStateException("Connection was not accepted", e);
    }
  }
}
