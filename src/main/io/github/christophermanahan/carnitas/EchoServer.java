package io.github.christophermanahan.carnitas;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer implements Server {

  private ServerSocket serverSocket;
  private Connection socketConnection;
  private Input socketInput;
  private Output socketOutput;

  EchoServer(ServerSocket serverSocket) {
    this.serverSocket = serverSocket;
  }

  public void run() {
    acceptConnection();

    echoUntilDisconnect();
  }

  private void acceptConnection() {
    try {
      Socket socket = serverSocket.accept();
      setup(socket);
    } catch (IOException e) {
      print("Server connection failed due to: " + e);
    }
  }

  private void setup(Socket socket) {
    socketConnection = new SocketConnection(socket);
    socketInput = new SocketInput(socket);
    socketOutput = new SocketOutput(socket);
  }

  private void echoUntilDisconnect() {
    while(connected()) {
      try {
        echo();
      } catch (SocketInput.InputStreamFailed e) {
        print(e.dueTo());
      } catch (SocketOutput.SendToSocketFailed e) {
        print(e.dueTo());
      } catch (SocketConnection.CloseSocketFailed e) {
        print(e.dueTo());
      }
    }
  }

  private boolean connected() {
    return socketConnection.isOpen();
  }

  private void echo() {
    socketInput.receive()
      .ifPresentOrElse(socketOutput::send, socketConnection::close);
  }

  private void print(String message) {
    System.out.println(message);
  }
}
