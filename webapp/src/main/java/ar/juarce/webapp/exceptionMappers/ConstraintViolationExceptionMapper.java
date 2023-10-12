package ar.juarce.webapp.exceptionMappers;

import ar.juarce.models.dtos.ValidationErrorDto;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
@Component
@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {
        List<ValidationErrorDto> errors = new ArrayList<>();

        e.getConstraintViolations().forEach(violation -> errors.add(ValidationErrorDto.fromValidationError(getViolationPropertyName(violation), violation.getMessage())));

        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new GenericEntity<Collection<ValidationErrorDto>>(errors) {
                })
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private String getViolationPropertyName(ConstraintViolation<?> violation) {
        final String propertyPath = violation.getPropertyPath().toString();
        return propertyPath.substring(propertyPath.lastIndexOf(".") + 1);
    }
}
