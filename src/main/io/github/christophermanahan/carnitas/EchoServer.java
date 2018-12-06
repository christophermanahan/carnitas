package io.github.christophermanahan.carnitas;

import java.io.IOException;
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

      Connection socketConnection = new SocketConnection(socket);
      Input socketInput = new SocketInput(socket);
      Output socketOutput = new SocketOutput(socket);

      while(socketConnection.isOpen()) {
        try {
          socketInput.receive()
            .ifPresentOrElse(socketOutput::send, socketConnection::close);
        } catch (SocketOutput.SendToSocketFailed e) {
          System.out.println(e.dueTo());
          continue;
        } catch (SocketConnection.CloseSocketFailed e) {
          System.out.println(e.dueTo());
        }
      }
    } catch (IOException e) {
      System.out.println("Server connection failed due to: " + e);
    }
  }
}
