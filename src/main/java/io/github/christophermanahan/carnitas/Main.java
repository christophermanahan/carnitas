package io.github.christophermanahan.carnitas;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {

  public static void main(String[] args) throws IOException {
    int port = args.length == 0  ? 33333 : Integer.parseInt(args[0]);
    ServerSocket serverSocket = new ServerSocket(port);
    new HTTPServer(new ServerSocketListener(serverSocket), new ErrorLogger()).run();
  }
}
