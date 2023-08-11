package ar.juarce.webapp.controllers;

import ar.juarce.interfaces.UserService;
import ar.juarce.models.User;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Path("/users")
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Path("/")
    @Produces("application/json")
    public Response getUsers() {
        logger.info("getUsers");
        final List<User> users = userService.getUsers();
        return Response.ok(new GenericEntity<>(users) {
        }).build();
    }

}