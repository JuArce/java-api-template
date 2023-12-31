package ar.juarce.webapp.exceptionMappers;

import ar.juarce.interfaces.exceptions.NotFoundException;
import ar.juarce.webapp.dtos.ErrorDto;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(ErrorDto.fromErrorMsg(exception.getMessage()))
                .build();
    }
}
