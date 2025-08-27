package com.example.demo.Controllers;

import com.example.demo.DTOs.CarResponse;
import com.example.demo.DTOs.SaleRequest;
import com.example.demo.DTOs.SaleResponse;
import com.example.demo.Services.SaleServices;
import com.example.demo.entity.Car;
import com.example.demo.entity.Sale;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@Tag(name = "Sales", description = " Sales Management APIs ")
@SecurityRequirement(name = "bearerAuth") // applies to all endpoints in this controller
@RestController
@RequestMapping("/api/sales")
public class SaleController {

    @Autowired
    private SaleServices saleService;

    // ✅ Helper to map entity -> response
    private SaleResponse mapToResponse(Sale sale) {
        return new SaleResponse(
                sale.getId(),
                sale.getDate(),
                sale.getPricesold(),
                sale.getCar().getId(),
                sale.getCustomer().getId(),
                sale.getSalesperson().getId()
        );
    }

    @Operation(summary = "Get all sales", description = "Retrieve a list of sales ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Sales report obtained successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<SaleResponse>> getAllSales() {
        List<Sale> sales = saleService.getsalesrecord();
        List<SaleResponse> responses = sales.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get a sale report", description = "Retrieve info of a specific sale report")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sale report obtained successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<SaleResponse> getSaleById(@PathVariable int id) {
        Sale sale = saleService.getsalebyid(id);
        return ResponseEntity.ok(mapToResponse(sale));
    }

@Operation(summary = "Create a sale report", description = "Add a new sale report ")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sale report added successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CarResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Unauthorized",
                content = @Content)
})
@PostMapping
    public ResponseEntity<SaleResponse> createSale(@Valid @RequestBody SaleRequest saleRequest) {
        Sale newSale = saleService.createnewsalerecord(
                saleRequest.getCarId(),
                saleRequest.getCustomerId(),
                saleRequest.getSalespersonId(),
                saleRequest.getPriceSold()
        );
        return ResponseEntity.ok(mapToResponse(newSale));
    }

    @GetMapping("/paginated")
    public Page<Sale> getSalesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "make") String sortBy
    ) {
        return saleService.getSalesPaginated(page, size, sortBy);
    }

    @Operation(summary = "Update a sale report", description = "Modify a specific info in the sale report")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sale report updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<SaleResponse> updateSale(@PathVariable int id, @Valid @RequestBody SaleRequest saleRequest) {
        Sale updatedSale = saleService.updatesalerecordinfo(
                id,
                saleRequest.getCarId(),
                saleRequest.getCustomerId(),
                saleRequest.getSalespersonId(),
                saleRequest.getPriceSold()
        );
        return ResponseEntity.ok(mapToResponse(updatedSale));
    }

    @Operation(summary = "Delete a sale report", description = " Remove a sale report from the list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sale Report deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSale(@PathVariable int id) {
        saleService.deletesalerecord(id);
        return ResponseEntity.ok("Sale record deleted successfully.");
    }
}
