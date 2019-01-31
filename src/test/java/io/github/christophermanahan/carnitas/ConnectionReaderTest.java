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
        Readable connection = new ReadConnection(request + Serializer.CRLF);
        ConnectionReader reader = new ConnectionReader(connection);

        Optional<String> read = reader.readUntil(Serializer.CRLF);

        assertEquals(Optional.of(request), read);
    }

    @Test
    void itReadsANumberOfCharacters() {
        String request = "GET";
        int numberOfCharacters = 3;
        Readable connection = new ReadConnection(request + Serializer.CRLF);
        Reader reader = new ConnectionReader(connection);

        Optional<String> read = reader.read(numberOfCharacters);

        assertEquals(Optional.of(request.substring(0, numberOfCharacters)), read);

    }

    @Test
    void itIsEmptyIfReadIsEmptyWhileReadingUntil() {
        Readable readable = new Readable() {
            public Optional<Character> read() {
                return Optional.empty();
            }

            public Optional<byte[]> readAll() {
                return Optional.empty();
            }
        };
        Reader reader = new ConnectionReader(readable);

        Optional<String> read = reader.readUntil(Serializer.CRLF);

        assertEquals(Optional.empty(), read);
    }

    @Test
    void itIsEmptyIfReadIsEmptyWhileReadingANumberOfCharacter() {
        Readable readable = new Readable() {
            public Optional<Character> read() {
                return Optional.empty();
            }

            public Optional<byte[]> readAll() {
                return Optional.empty();
            }
        };
        Reader reader = new ConnectionReader(readable);

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

        public Optional<byte[]> readAll() {
            return Optional.empty();
        }
    }
}
