package com.example.demo.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;

public class CarResponse {

    private int id;
    @Schema(description = "Car manufacturer", example = "Toyota")
    private String make;
    @Schema(description = "Car model", example = "corolla")
    private String model;
    @Schema(description = "Year it got manufactured", example = "2021")
    private int year;
    @Schema(description = "Car price in USD", example = "70000")
    private double price;

    public CarResponse(int id, String make, String model, int year, double price) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.price = price;
    }

    // ✅ Getters
    public int getId() { return id; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getPrice() { return price; }
}
