package ar.juarce.webapp.dtos;

import ar.juarce.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UserDto(
        Long id,

        @NotNull
        @Email
        @Size(min = 3, max = 255)
        String email,

        @NotNull
        @Pattern(regexp = "^[a-zA-Z0-9_-]+$")
        @Size(min = 3, max = 255)
        String username,

        @NotNull
        @Size(min = 8, max = 255)
        String password,

        boolean enabled,

        String createdAt) {

    public static UserDto fromUser(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                null,
                user.isEnabled(),
                user.getCreatedAt().toString()
        );
    }

    public static List<UserDto> fromUsers(List<User> users) {
        return users.stream().map(UserDto::fromUser).toList();
    }

}
