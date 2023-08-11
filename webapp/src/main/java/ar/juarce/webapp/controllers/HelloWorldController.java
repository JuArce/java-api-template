package ar.juarce.webapp.controllers;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/hello")
public class HelloWorldController {

    @GET
    public Response hello() {
        return Response.ok("Hello World").build();
    }
}
