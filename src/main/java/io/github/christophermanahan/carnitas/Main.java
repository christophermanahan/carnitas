package io.github.christophermanahan.carnitas;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {

  public static void main(String[] args) throws IOException {
    ServerSocket serverSocket = new ServerSocket(33333);
    new EchoServer(new ServerSocketListener(serverSocket), new ErrorLogger()).run();
  }
}
