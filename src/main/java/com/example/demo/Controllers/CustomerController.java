package com.example.demo.Controllers;

import com.example.demo.DTOs.CarResponse;
import com.example.demo.DTOs.CustomerRequest;
import com.example.demo.DTOs.CustomerResponse;
import com.example.demo.entity.Car;
import com.example.demo.entity.Customer;
import com.example.demo.Services.CustomerServices;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Customer", description = "Customer management APIs")
@SecurityRequirement(name = "bearerAuth") // applies to all endpoints in this controller
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerServices customerService;

    @Operation(summary = "Get all customers", description = "Retrieve all the registered customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Customers Obtained successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAll() {
        List<Customer> list = customerService.getallcustomer();
        List<CustomerResponse> response = list.stream()
                .map(c -> new CustomerResponse(c.getId(), c.getName(), c.getEmail(), c.getPhone()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get customer", description = "Get a specific customer info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer obtained successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable int id) {
        Customer c = customerService.getcustomerbyid(id);
        return ResponseEntity.ok(new CustomerResponse(c.getId(), c.getName(), c.getEmail(), c.getPhone()));
    }

    @Operation(summary = "Add a new customer", description = "Add a new customer to the list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New Customer added successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerRequest req) {
        Customer c = new Customer();
        c.setName(req.getName());
        c.setEmail(req.getEmail());
        c.setPhone(req.getPhone());
        Customer saved = customerService.createnewcustomer(c);
        return ResponseEntity.ok(new CustomerResponse(saved.getId(), saved.getName(), saved.getEmail(), saved.getPhone()));
    }

    @GetMapping("/paginated")
    public Page<Customer> getCustomersPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "make") String sortBy
    ) {
        return customerService.getCustomersPaginated(page, size, sortBy);
    }

    @Operation(summary = "Update customer info", description = "Update the info of a registered customer" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer info updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(@PathVariable int id, @Valid @RequestBody CustomerRequest req) {
        Customer newc = new Customer();
        newc.setName(req.getName());
        newc.setEmail(req.getEmail());
        newc.setPhone(req.getPhone());
        Customer updated = customerService.updatecustomerdetails(id, newc);
        return ResponseEntity.ok(new CustomerResponse(updated.getId(), updated.getName(), updated.getEmail(), updated.getPhone()));
    }

    @Operation(summary = "Delete a customer", description = "Remove a customer from the list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        customerService.getcustomerbyid(id);
        customerService.deletecustomer(id);
        return ResponseEntity.ok("Customer deleted successfully.");
    }
}
