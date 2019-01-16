package io.github.christophermanahan.carnitas;

public interface Reader {
    String readUntil(String delimiter);
    String read(int numberOfCharacters);
}
