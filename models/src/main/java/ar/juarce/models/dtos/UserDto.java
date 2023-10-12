package ar.juarce.models.dtos;

import ar.juarce.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UserDto(
        Long id,

        @NotNull
        @Pattern(regexp = "^[a-zA-Z0-9_-]+$")
        @Size(min = 3, max = 255)
        String username,

        @NotNull
        @Email
        @Size(min = 3, max = 255)
        String email,

        @NotNull
        @Size(min = 8, max = 255)
        String password,

        boolean enabled,

        String createdAt) {

    public static UserDto fromUser(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                null,
                user.isEnabled(),
                user.getCreatedAt().toString()
        );
    }

    public static List<UserDto> fromUsers(List<User> users) {
        return users.stream().map(UserDto::fromUser).toList();
    }

}
