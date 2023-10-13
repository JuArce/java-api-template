package ar.juarce.webapp.controllers;

import ar.juarce.interfaces.UserService;
import ar.juarce.interfaces.exceptions.AlreadyExistsException;
import ar.juarce.models.User;
import ar.juarce.webapp.dtos.UserDto;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Path("/users")
public class UserController {
    private final UserService userService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        final List<User> users = userService.findAll();

        if (users.isEmpty()) {
            return Response
                    .noContent()
                    .build();
        }

        return Response
                .ok(UserDto.fromUsers(users))
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveUser(@Valid UserDto userDto) throws AlreadyExistsException {
        final User user = userService.create(buildNewUser(userDto));

        return Response
                .created(uriInfo
                        .getAbsolutePathBuilder()
                        .path(user.getId().toString())
                        .build())
                .entity(UserDto.fromUser(user))
                .build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") Long id) {
        final User user = userService.findById(id).orElseThrow(NotFoundException::new);
        return Response
                .ok(UserDto.fromUser(user))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") Long id, @Valid UserDto userDto) throws AlreadyExistsException {
        final User user = userService.update(id, buildNewUser(userDto));
        if (user.getId().equals(id)) {
            return Response
                    .ok(UserDto.fromUser(user))
                    .build();
        }

        return Response
                .created(uriInfo
                        .getAbsolutePathBuilder()
                        .path(user.getId().toString())
                        .build())
                .entity(UserDto.fromUser(user))
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        userService.deleteById(id);
        return Response
                .noContent()
                .build();
    }

    /*
    Auxiliary methods
     */
    private User buildNewUser(UserDto userDto) {
        return User.builder()
                .username(userDto.username())
                .email(userDto.email())
                .password(userDto.password())
                .build();
    }

}
