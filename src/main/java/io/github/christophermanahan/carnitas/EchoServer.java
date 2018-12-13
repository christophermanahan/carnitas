package io.github.christophermanahan.carnitas;

public class EchoServer {

  private final Listener listener;
  private final Logger errorLogger;
  private Connection connection;

  public EchoServer(Listener listener, Logger errorLogger) {
    this.listener = listener;
    this.errorLogger = errorLogger;
  }

  public void run() {
    try {
      connect();
      echo();
      close();
    } catch (RuntimeException e) {
      errorLogger.log(e.getMessage());
    }
  }

  private void connect() {
    connection = listener.listen();
  }

  private void echo() {
    connection.receive()
      .ifPresent(connection::send);
  }

  private void close() {
    connection.close();
  }
}
