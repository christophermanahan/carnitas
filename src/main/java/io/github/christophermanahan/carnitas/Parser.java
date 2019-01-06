package io.github.christophermanahan.carnitas;

import java.util.Optional;

public interface Parser {

    public Optional<String> parse(Receiver receiver);
}
