package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConnectionReaderTest {
    @Test
    void itReadsCharactersUpToADelimiter() {
        String request = "GET";
        Readable connection = new ReadConnection(request + Response.CRLF);
        ConnectionReader reader = new ConnectionReader(connection);

        Optional<String> read = reader.readUntil(Response.CRLF);

        assertEquals(Optional.of(request), read);
    }

    @Test
    void itReadsANumberOfCharacters() {
        String request = "GET";
        int numberOfCharacters = 3;
        Readable connection = new ReadConnection(request + Response.CRLF);
        Reader reader = new ConnectionReader(connection);

        Optional<String> read = reader.read(numberOfCharacters);

        assertEquals(Optional.of(request.substring(0, numberOfCharacters)), read);

    }

    @Test
    void itIsEmptyIfReadIsEmptyWhileReadingUntil() {
        Reader reader = new ConnectionReader(Optional::empty);

        Optional<String> read = reader.readUntil(Response.CRLF);

        assertEquals(Optional.empty(), read);
    }

    @Test
    void itIsEmptyIfReadIsEmptyWhileReadingANumberOfCharacter() {
        Reader reader = new ConnectionReader(Optional::empty);

        Optional<String> read = reader.read(1);

        assertEquals(Optional.empty(), read);
    }

    private class ReadConnection implements Readable {
        private final Iterator<String> request;

        ReadConnection(String request) {
            this.request = List.of(request.split("")).iterator();
        }

        public Optional<Character> read() {
            return Optional.of(request.next().charAt(0));
        }
    }
}
