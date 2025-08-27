package com.example.demo.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class UserRequest {

    @Schema(description = "Username for th User", example = "SAM")
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(description = "Password for the Account", example = "1234")
    @NotBlank(message = "Password is required")
    private String password;

    @Schema(description = "Role of the User", example = "ADMIN")
    @NotBlank(message = "Role is required")
    private String role;

    // ✅ Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
