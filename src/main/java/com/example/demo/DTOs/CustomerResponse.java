package com.example.demo.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;

public class CustomerResponse {
    private int id;

    @Schema(description = "Name of the Customer", example = "abed")
    private String name;

    @Schema(description = "Email of the Customer", example = "abed@gmail.com")
    private String email;

    @Schema(description = "Phone number", example = "961 123 456 789")
    private String phone;

    public CustomerResponse(int id,  String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
}
