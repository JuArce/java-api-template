package ar.juarce.webapp.dtos;

public record ValidationErrorDto(String attribute, String message) {

    public static ValidationErrorDto fromValidationError(String attribute, String message) {
        return new ValidationErrorDto(attribute, message);
    }
}
