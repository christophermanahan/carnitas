package io.github.christophermanahan.carnitas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConnectionReceiver implements Receiver {

    private final InputStream inputStream;

    public ConnectionReceiver(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String receiveLine() {
        try {
            return new BufferedReader(new InputStreamReader(inputStream)).readLine();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public String receiveCharacters(int amount) {
        try {
            char[] characters = new char[amount];
            new BufferedReader(new InputStreamReader(inputStream)).read(characters);
            return new String(characters);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
