package ar.juarce.models;

import lombok.Data;

import java.util.Objects;

@Data
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;

    public User(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = encrypt(password);
    }

    public User() {
        // Needed for Jackson deserialization
    }

    private String encrypt(String password) {
        return password + "_encrypted";
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
        return "User [email=" + email + ", id=" + id + ", name=" + name + ", password=" + password + "]";
    }
}
