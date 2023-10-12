package ar.juarce.models;

import ar.juarce.models.dtos.UserDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "created_at" ,nullable = false)
    private LocalDateTime createdAt;

    public User() {
        // Needed for Hibernate
    }

    public User(Long id, String username, String email, String password, boolean enabled, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.createdAt = createdAt;
    }

    public static User fromUserDto(UserDto userDto) {
        return new Builder()
                .id(null)
                .username(userDto.username())
                .email(userDto.email())
                .password(userDto.password())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("User %s: [email = %s, name = %s]", id, email, username);
    }

    /*
        Builder for User
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final User user;

        public Builder() {
            user = new User();
            // Set default values
            user.setEnabled(false);
            user.setCreatedAt(LocalDateTime.now());
        }

        public User build() {
            return user;
        }

        public Builder id(Long id) {
            user.setId(id);
            return this;
        }

        public Builder username(String username) {
            user.setUsername(username);
            return this;
        }

        public Builder email(String email) {
            user.setEmail(email);
            return this;
        }

        public Builder password(String password) {
            user.setPassword(password);
            return this;
        }

        public Builder enabled(boolean enabled) {
            user.setEnabled(enabled);
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            user.setCreatedAt(createdAt);
            return this;
        }
    }
}
