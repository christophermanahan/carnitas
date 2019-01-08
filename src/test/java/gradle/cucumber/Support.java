package gradle.cucumber;

import io.github.christophermanahan.carnitas.Main;

import java.io.IOException;

public class Support implements Runnable {
    private final String port;

    Support(String port) {
        this.port = port;
    }

    public void run() {
        String[] args = {port};
        try {
            Main.main(args);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
