package io.github.christophermanahan.carnitas;

public class Header {
    private final String value;

    Header(String value) {
        this.value = value;
    }

    String value() {
        return value;
    }
}
