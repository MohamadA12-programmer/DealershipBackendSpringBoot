package com.example.demo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class AuthRequest {


    @Schema(description = "Name Of User", example = "SAM")
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(description = "Password for Account", example = "1234")
    @NotBlank(message = "Password is required")
    private String password;

    // ✅ Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
