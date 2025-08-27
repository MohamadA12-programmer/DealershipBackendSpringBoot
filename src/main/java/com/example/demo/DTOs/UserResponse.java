package com.example.demo.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserResponse {
    private int id;
    @Schema(description = "Username for th User", example = "SAM")
    private String username;
    @Schema(description = "Role of the User", example = "ADMIN")
    private String role;

    public UserResponse(int id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
}
