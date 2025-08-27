package com.example.demo.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public class SaleResponse {
    private int id;

    private LocalDate date;

    @Schema(description = "Price Sold to the customer", example = "2500$")
    private double priceSold;
    @Schema(description = "Car ID", example ="6")
    private int carId;
    @Schema(description = "Customer ID", example = "1")
    private int customerId;
    @Schema(description = "Salesperson ID", example = "1")
    private int salespersonId;

    public SaleResponse(int id, LocalDate date, double priceSold, int carId, int customerId, int salespersonId) {
        this.id = id;
        this.date = date;
        this.priceSold = priceSold;
        this.carId = carId;
        this.customerId = customerId;
        this.salespersonId = salespersonId;
    }

    public int getId() { return id; }
    public LocalDate getDate() { return date; }
    public double getPriceSold() { return priceSold; }
    public int getCarId() { return carId; }
    public int getCustomerId() { return customerId; }
    public int getSalespersonId() { return salespersonId; }
}
