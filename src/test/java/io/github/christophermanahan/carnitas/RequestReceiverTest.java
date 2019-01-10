package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestReceiverTest {
    @Test
    void receivesDataFromStream() {
        String request = "GET /simple_get HTTP/1.1";

        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(request.getBytes())));
        String received = new RequestReceiver(reader).receiveLine();

        assertEquals(request, received);
    }

    @Test
    void receivesCharacterAmountFromStream() {
        String request = "GET /simple_get HTTP1.1";
        int amount = 3;

        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(request.getBytes())));
        String received = new RequestReceiver(reader).receiveCharacters(amount);

        assertEquals(request.substring(0, amount), received);
    }

    @Test
    void throwsExceptionIfReadLineFails() {
        class ReadLineExceptionReader extends BufferedReader {
            private ReadLineExceptionReader(Reader in) {
                super(in);
            }

            public String readLine() throws IOException {
                throw new IOException();
            }
        }
        String request = "GET /simple_get HTTP1.1";

        Receiver receiver = new RequestReceiver(new ReadLineExceptionReader(new StringReader(request)));

        assertThrows(RuntimeException.class, receiver::receiveLine);
    }

    @Test
    void throwsExceptionIfReadCharactersFails() {
        class ReadExceptionReader extends BufferedReader {
            ReadExceptionReader(Reader in) {
                super(in);
            }

            public int read(char[] cbuf) throws IOException {
                throw new IOException();
            }
        }
        String request = "GET /simple_get HTTP1.1";

        Receiver receiver = new RequestReceiver(new ReadExceptionReader(new StringReader(request)));

        assertThrows(RuntimeException.class, ()->{ receiver.receiveCharacters(request.length()); } );
    }
}

