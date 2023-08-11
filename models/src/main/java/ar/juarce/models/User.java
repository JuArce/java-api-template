package ar.juarce.models;

import lombok.Data;
@Data
public class User {
    private final int id;
    private final String name;
    private final String email;
    private final String password;

    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = encrypt(password);
    }

    private String encrypt(String password) {
        return password + "_encrypted";
    }

    @Override
    public String toString() {
        return "User [email=" + email + ", id=" + id + ", name=" + name + ", password=" + password + "]";
    }
}
