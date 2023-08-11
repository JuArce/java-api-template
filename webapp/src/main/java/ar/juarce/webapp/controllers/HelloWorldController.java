package ar.juarce.webapp.controllers;

import ar.juarce.interfaces.UserService;
import ar.juarce.models.User;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Path("/hello")
@Service
public class HelloWorldController {

    private final UserService userService;

    @Autowired
    public HelloWorldController(UserService userService) {
        this.userService = userService;
    }

    @GET
    public Response hello() {
        final List<User> users = userService.getUsers();
        return Response.ok(users.get(0).getEmail()).build();
    }
}
