package io.github.christophermanahan.carnitas;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {

  public static void main(String[] args) throws IOException {
    int port = Integer.parseInt(args[0]);
    ServerSocket serverSocket = new ServerSocket(port);
    new EchoServer(new ServerSocketListener(serverSocket), new ErrorLogger()).run();
  }
}
