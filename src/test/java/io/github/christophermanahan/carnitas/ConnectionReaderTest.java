package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConnectionReaderTest {
    @Test
    void itReadsCharactersUpToADelimiter() {
        String request = "GET http://localhost:80/simple_get " + Constants.VERSION;
        Readable connection = new ReadConnection(request + Constants.CRLF);
        ConnectionReader reader = new ConnectionReader(connection);

        Optional<String> read = reader.readUntil(Constants.CRLF);

        assertEquals(Optional.of(request), read);
    }

    @Test
    void itReadsANumberOfCharacters() {
        String request = "GET http://localhost:80/simple_get " + Constants.VERSION;
        int numberOfCharacters = 3;
        Readable connection = new ReadConnection(request + Constants.CRLF);
        Reader reader = new ConnectionReader(connection);

        Optional<String> read = reader.read(numberOfCharacters);

        assertEquals(Optional.of(request.substring(0, numberOfCharacters)), read);

    }

    private class ReadConnection implements Readable {
        private final Iterator<String> request;

        ReadConnection(String request) {
            this.request = List.of(request.split("")).iterator();
        }

        public char read() {
            return request.next().charAt(0);
        }
    }
}
