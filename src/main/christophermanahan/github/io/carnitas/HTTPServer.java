package christophermanahan.github.io.carnitas;

import java.net.ServerSocket;

public class HTTPServer implements Server {

  final ServerSocket serverSocket;

  HTTPServer(ServerSocket serverSocket) {
    this.serverSocket = serverSocket;
  }

  public void run() {

  }
}
