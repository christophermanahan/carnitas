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
    } catch (AcceptConnectionException e) {
      errorLogger.log(ErrorMessages.ACCEPT_CONNECTION);
    } catch (SendToConnectionException e) {
      errorLogger.log(ErrorMessages.SEND_TO_CONNECTION);
    } catch (ConnectionCloseException e) {
      errorLogger.log(ErrorMessages.CLOSE_CONNECTION);
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
