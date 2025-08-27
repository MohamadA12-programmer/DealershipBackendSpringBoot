package com.example.demo.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class SaleRequest {

    @Schema(description = "Car ID", example ="6")
    @NotNull(message = "Car ID is required")
    private Integer carId;

    @Schema(description = "Customer ID", example = "1")
    @NotNull(message = "Customer ID is required")
    private Integer customerId;

    @Schema(description = "Salesperson ID", example = "1")
    @NotNull(message = "Salesperson ID is required")
    private Integer salespersonId;

    private LocalDate saleDate;

    @Schema(description = "Price Sold to the customer", example = "2500$")
    @Min(value = 1, message = "Price sold must be greater than 0")
    private double priceSold;

    // ✅ Getters and Setters
    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getSalespersonId() {
        return salespersonId;
    }

    public void setSalespersonId(Integer salespersonId) {
        this.salespersonId = salespersonId;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public double getPriceSold() {
        return priceSold;
    }

    public void setPriceSold(double priceSold) {
        this.priceSold = priceSold;
    }
}
