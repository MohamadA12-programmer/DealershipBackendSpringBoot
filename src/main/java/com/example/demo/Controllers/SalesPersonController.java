package com.example.demo.Controllers;

import com.example.demo.DTOs.CarResponse;
import com.example.demo.DTOs.SalesPersonResponse;
import com.example.demo.entity.Car;
import com.example.demo.entity.SalesPerson;
import com.example.demo.Services.SalesPersonServices;
import com.example.demo.Exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@Tag(name="SalesPerson", description = "SalesPerson Management APIs")
@SecurityRequirement(name = "bearerAuth") // applies to all endpoints in this controller
@RestController
@RequestMapping("/api/salespersons")
public class SalesPersonController {

    @Autowired
    private SalesPersonServices salesPersonService;

    @Operation(summary = "Get all salespersons", description = "Retrieve all the Salespersons info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Salespersons retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<SalesPersonResponse>> getAllSalespersons() {
        List<SalesPerson> salespersons = salesPersonService.getallSalesPerson();
        List<SalesPersonResponse> responses = salespersons.stream()
                .map(sp -> new SalesPersonResponse(sp.getId(), sp.getName(), sp.getEmail()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

@Operation(summary = "Get a specific Salesperson", description = "Retrieve info on a specific salesperson")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "salesperson retrieved successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CarResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Unauthorized",
                content = @Content)
})
@GetMapping("/{id}")
    public ResponseEntity<SalesPersonResponse> getSalespersonById(@PathVariable int id) {
        SalesPerson sp = salesPersonService.getSalesPersonbyID(id); // throws exception if not found
        SalesPersonResponse response = new SalesPersonResponse(sp.getId(), sp.getName(), sp.getEmail());
        return ResponseEntity.ok(response);
    }

@Operation(summary = "Create a new Salesperson", description = "Add a new Salesperson")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "New Salesperson added successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CarResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Unauthorized",
                content = @Content)
})
@PostMapping
    public ResponseEntity<SalesPersonResponse> createSalesPerson(@RequestBody SalesPerson salesPerson) {
        SalesPerson saved = salesPersonService.createnewSalesPerson(salesPerson);
        SalesPersonResponse response = new SalesPersonResponse(saved.getId(), saved.getName(), saved.getEmail());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated")
    public Page<SalesPerson> getSalespersonsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "make") String sortBy
    ) {
        return salesPersonService.getSalespersonsPaginated(page, size, sortBy);
    }

@Operation(summary = "Update a salesperson", description = "Change info a specific salesperson")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Salesperson inf updated successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CarResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Unauthorized",
                content = @Content)
})
@PutMapping("/{id}")
    public ResponseEntity<SalesPersonResponse> updateSalesPerson(@PathVariable int id, @RequestBody SalesPerson salesPerson) {
        SalesPerson updated = salesPersonService.updateSalesPinfo(id, salesPerson);
        if (updated == null) {
            throw new ResourceNotFoundException("Salesperson with id " + id + " not found");
        }
        SalesPersonResponse response = new SalesPersonResponse(updated.getId(), updated.getName(), updated.getEmail());
        return ResponseEntity.ok(response);
    }

@Operation(summary = "Delete a Salesperson", description = "Remove a salesperson from the dealership")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Salesperson deleted successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CarResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Unauthorized",
                content = @Content)
})
@DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSalesPerson(@PathVariable int id) {
        SalesPerson sp = salesPersonService.getSalesPersonbyID(id);
        if (sp == null) {
            throw new ResourceNotFoundException("Salesperson with id " + id + " not found");
        }
        salesPersonService.deletesalesPersonbyId(id);
        return ResponseEntity.ok("Salesperson deleted successfully.");
    }
}
