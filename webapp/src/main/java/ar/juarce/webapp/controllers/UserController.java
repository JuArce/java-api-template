package ar.juarce.webapp.controllers;

import ar.juarce.interfaces.UserService;
import ar.juarce.models.User;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Path("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Path("/")
    @Produces("application/json")
    public Response getUsers() {
        final List<User> users = userService.findAll();
        return Response.ok(new GenericEntity<>(users) {
        }).build();
    }

}
