package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServerLoggerTest {
    @Test
    void itLogsErrorMessagesAppendedWithNewLine() {
        String error = "Failed due to <error>";
        OutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        Logger logger = new ServerLogger();

        logger.log(error);

        assertEquals(error, output.toString().strip());
    }

    @Test
    void itLogsRequests() {
        Request request = new Request(Request.Method.GET, "/simple_get")
          .withBody(Optional.of("name=<something>"));
        OutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        Logger logger = new ServerLogger();

        logger.log(request);

        assertEquals(request.toString(), output.toString().strip());
    }

    @Test
    void itProvidesRequest() {
        Request request = new Request(Request.Method.GET, "/simple_get")
          .withBody(Optional.of("name=<something>"));
        Logger logger = new ServerLogger();

        Request logged = logger.log(request);

        assertEquals(request, logged);
    }

    @Test
    void itLogsResponses() {
        Response response = new ResponseBuilder()
          .set(Response.Status.OK)
          .get();
        OutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        Logger logger = new ServerLogger();

        logger.log(response);

        assertEquals(response.toString(), output.toString().strip());
    }

    @Test
    void itProvidesResponse() {
        Response response = new ResponseBuilder()
          .set(Response.Status.OK)
          .get();
        Logger logger = new ServerLogger();

        Response logged = logger.log(response);

        assertEquals(response, logged);
    }
}
