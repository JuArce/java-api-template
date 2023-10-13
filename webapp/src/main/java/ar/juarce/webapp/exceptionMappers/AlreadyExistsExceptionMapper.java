package ar.juarce.webapp.exceptionMappers;

import ar.juarce.interfaces.exceptions.AlreadyExistsException;
import ar.juarce.webapp.dtos.ErrorDto;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AlreadyExistsExceptionMapper implements ExceptionMapper<AlreadyExistsException> {

    @Override
    public Response toResponse(AlreadyExistsException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(ErrorDto.fromErrorMsg(exception.getMessage()))
                .build();
    }
}
