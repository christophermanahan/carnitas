package gradle.cucumber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Request {

  private final Socket socket;

  public Request(String address, String port) throws IOException {
    this.socket = new Socket(address, Integer.parseInt(port));
  }

  public Response send(String data) throws IOException {
    BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    new PrintWriter(socket.getOutputStream(), true).println(data);
    return new Response(receive.readLine());
  }
}
