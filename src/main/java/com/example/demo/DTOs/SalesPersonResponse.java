package com.example.demo.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;

public class SalesPersonResponse {
    private int id;
    @Schema(description = "Name of the Salesperson", example = "Samir")
    private String name;
    @Schema(description = "Email of the Salesperson", example = "samir@gmail.com")
    private String email;

    public SalesPersonResponse(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email;}

}
