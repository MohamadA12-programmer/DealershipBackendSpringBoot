package com.example.demo.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CustomerRequest {
    @Schema(description = "Name of the Customer", example = "abed")
    @NotBlank(message = "Customer name is required")
    private String name;

    @Schema(description = "Email of the Customer", example = "abed@gmail.com")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "Phone number", example = "961 123 456 789")
    @NotBlank(message = "Phone is required")
    private String phone;

    // Getters & Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
