package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConnectionReceiverTest {

    @Test
    void receivesDataFromStream() {
        String request = "GET /simple_get HTTP1.1";

        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(request.getBytes())));
        String received = new ConnectionReceiver(reader).receiveLine();

        assertEquals(request, received);
    }

    @Test
    void receivesCharacterAmountFromStream() {
        String request = "GET /simple_get HTTP1.1";
        int amount = 3;

        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(request.getBytes())));
        String received = new ConnectionReceiver(reader).receiveCharacters(amount);

        assertEquals(request.substring(0, amount), received);
    }
}
