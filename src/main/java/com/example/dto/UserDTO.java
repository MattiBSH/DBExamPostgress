package com.example.dto;

public class UserDTO {
    final String username;
    final String email;
    final Long id;

    public UserDTO(String username, String email, Long id) {
        this.username = username;
        this.email = email;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }
}
