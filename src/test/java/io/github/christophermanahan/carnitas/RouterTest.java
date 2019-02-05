package io.github.christophermanahan.carnitas;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RouterTest {
    @Test
    void itProcessesAGETRequestIntoAResponseIfTheRouteHasBeenAdded() {
        Function<Request, Response> handler = (Request request) -> new ResponseBuilder()
          .set(Response.Status.OK)
          .get();
        Request request = new Request(Request.Method.GET, "/simple_get");
        Router router = new Router()
          .get("/simple_get", handler);

        Response response = router.handle(request);

        assertEquals(handler.apply(request), response);
    }

    @Test
    void itProcessesAHEADRequestIntoAResponseIfTheRouteHasBeenAdded() {
        Function<Request, Response> handler = (Request request) -> new ResponseBuilder()
          .set(Response.Status.OK)
          .get();
        Request request = new Request(Request.Method.HEAD, "/simple_get");
        Router router = new Router()
          .head("/simple_get", handler);

        Response response = router.handle(request);

        assertEquals(handler.apply(request), response);
    }

    @Test
    void itProcessesAPOSTRequestIntoAResponseIfTheRouteHasBeenAdded() {
        Function<Request, Response> handler = (Request request) -> new ResponseBuilder()
          .set(Response.Status.CREATED)
          .get();
        Request request = new Request(Request.Method.POST, "/simple_post");
        Router router = new Router()
          .post("/simple_post", handler);

        Response response = router.handle(request);

        assertEquals(handler.apply(request), response);
    }

    @Test
    void itProcessesARequestIntoAResponseIfMultipleRoutesHaveBeenAdded() {
        Function<Request, Response> handler = (Request request) -> new ResponseBuilder()
          .set(Response.Status.OK)
          .get();
        Request request = new Request(Request.Method.GET, "simple_get_again");
        Router router = new Router()
          .get( "/simple_get", handler)
          .get("simple_get_again", handler);

        Response response = router.handle(request);

        assertEquals(handler.apply(request), response);
    }

    @Test
    void itProcessesARequestIntoANotFoundResponseIfTheRouteHasNotBeenAdded() {
        Function<Request, Response> handler = (Request request) -> new ResponseBuilder()
          .set(Response.Status.OK)
          .get();
        Request request = new Request(Request.Method.POST, "/simple_post");
        Router router = new Router()
          .get( "/simple_get", handler);

        Response response = router.handle(request);

        Response expectedResponse = new ResponseBuilder()
          .set(Response.Status.NOT_FOUND)
          .add(Headers.CONTENT_LENGTH + 0)
          .get();
        assertEquals(expectedResponse, response);
    }

    @Test
    void itDoesNotAllowRequestsToRoutesThatCannotRespondToTheRequestMethod() {
        Function<Request, Response> handler = (Request request) -> new ResponseBuilder()
          .set(Response.Status.CREATED)
          .get();
        Request request = new Request(Request.Method.GET, "/simple_post");
        Router router = new Router()
          .post("/simple_post", handler);

        Response response = router.handle(request);

        Response expectedResponse = new ResponseBuilder()
          .set(Response.Status.METHOD_NOT_ALLOWED)
          .add(Headers.ALLOW + Request.Method.OPTIONS + " " + Request.Method.POST)
          .add(Headers.CONTENT_LENGTH + 0)
          .get();
        assertEquals(expectedResponse, response);
    }

    @Test
    void itProcessesAnOPTIONSRequestIntoAResponse() {
        Response.Status status = Response.Status.OK;
        Function<Request, Response> handler = (Request request) -> new ResponseBuilder()
          .set(status)
          .get();
        Request request = new Request(Request.Method.OPTIONS, "/simple_get");
        Router router = new Router()
          .get( "/simple_get", handler);

        Response response = router.handle(request);


        Response expectedResponse = new ResponseBuilder()
          .set(Response.Status.OK)
          .add(Headers.CONTENT_LENGTH + 0)
          .add(Headers.ALLOW + Request.Method.GET + " " + Request.Method.OPTIONS)
          .get();
        assertEquals(expectedResponse, response);
    }
}
