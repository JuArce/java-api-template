package ar.juarce.webapp.controllers;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/")
public class HomeController {

    @GET
    public Response hello() {
        return Response.ok("Welcome to this API REST").build();
    }
}
