package io.github.christophermanahan.carnitas;

import java.util.Optional;

public interface Connection {
  void send(String data);

  Optional<String> receive();

  void close();
}
