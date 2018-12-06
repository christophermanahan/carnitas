package io.github.christophermanahan.carnitas;

public interface Connection {
  boolean isOpen();
  void close();
}
