package gradle.cucumber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Request {

  private final Socket socket;

  public Request(String port) throws IOException {
    this.socket = new Socket();
    socket.bind(new InetSocketAddress(InetAddress.getLocalHost(), 5000));
    socket.connect(new InetSocketAddress(Integer.parseInt(port)));
  }

  public String send(String data) throws IOException {
    BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    new PrintWriter(socket.getOutputStream(), true).println(data);
    return receive.readLine();
  }
}
