package com.example.demo.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class SalesPersonRequest {

    @Schema(description = "Name of the Salesperson", example = "Samir")
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(description = "Email of the Salesperson", example = "samir@gmail.com")
    @NotBlank(message = "Email is required")
    private String email;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
