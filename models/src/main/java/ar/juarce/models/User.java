package ar.juarce.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    public User() {
        // Needed for Hibernate
    }

    public void addRole(Role role) {
        roles.add(role);
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
            validate();
            return user;
        }

        public Builder id(Long id) {
            user.setId(id);
            return this;
        }

        public Builder email(String email) {
            user.setEmail(email);
            return this;
        }

        public Builder username(String username) {
            user.setUsername(username);
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

        public void validate() {
            Objects.requireNonNull(user.getUsername());
            Objects.requireNonNull(user.getEmail());
            Objects.requireNonNull(user.getPassword());
        }
    }
}
