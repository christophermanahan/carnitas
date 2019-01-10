package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestReceiverTest {
    @Test
    void receivesDataFromStream() {
        String request = "GET /simple_get HTTP/1.1";

        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(request.getBytes())));
        String received = new RequestReceiver(reader).receiveLine();

        assertEquals(request, received);
    }
}

