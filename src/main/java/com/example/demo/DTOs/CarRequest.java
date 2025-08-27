package com.example.demo.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CarRequest {

    @Schema(description = "Car manufacturer", example = "Toyota")
    @NotBlank(message = "Make is required")
    private String make;

    @Schema(description = "Car model", example = "corolla")
    @NotBlank(message = "Model is required")
    private String model;

    @Schema(description = "Year it got manufactured", example = "2021")
    @NotNull(message = "Year is required")
    @Min(value = 1886, message = "Year must be valid")
    private Integer year;

    @Schema(description = "Car price in USD", example = "70000")
    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Price must be greater than 0")
    private Double price;

    // ✅ Getters and Setters
    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
