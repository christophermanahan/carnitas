package christophermanahan.github.io.carnitas;

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
        socketInput.receive()
          .ifPresentOrElse(socketOutput::send, socketConnection::close);
      }
    } catch (IOException e) {
      throw new IllegalStateException("Connection was not accepted", e);
    }
  }
}
