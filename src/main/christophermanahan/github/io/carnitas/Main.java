package christophermanahan.github.io.carnitas;

import java.net.ServerSocket;
import java.io.IOException;

class Main {
  public static void main(String[] args) throws IOException {
    ServerSocket serverSocket = new ServerSocket(30000);
    EchoServer echoServer = new EchoServer(serverSocket);
    echoServer.run();
  }
}
