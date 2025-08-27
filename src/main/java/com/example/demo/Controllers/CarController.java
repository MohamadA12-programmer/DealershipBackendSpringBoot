package com.example.demo.Controllers;

import com.example.demo.DTOs.CarRequest;
import com.example.demo.DTOs.CarResponse;
import com.example.demo.entity.Car;
import com.example.demo.Services.CarServices;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;


@Tag(name = "Car", description = "Car management APIs")
@SecurityRequirement(name = "bearerAuth") // applies to all endpoints in this controller
@RestController
@RequestMapping("/api/cars")
public class CarController {

    @Autowired
    private CarServices carserv;

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @Operation(summary = "Get all cars", description = "Retrieve a list of all available cars")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cars Obtained successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<?> getAllCars() {
        List<Car> cars = carserv.getallcars();
        if (isAdmin()) return ResponseEntity.ok(cars);

        List<CarResponse> list = cars.stream()
                .map(car -> new CarResponse(car.getId(), car.getMake(), car.getModel(), car.getYear(), car.getPrice()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @Operation( summary = "Get a specific Car", description = "Retrieve info of a specific car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car info Obtained successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getCar(@PathVariable int id) {
        Car car = carserv.getCarbyId(id);
        if (isAdmin()) return ResponseEntity.ok(car);

        return ResponseEntity.ok(new CarResponse(car.getId(), car.getMake(), car.getModel(), car.getYear(), car.getPrice()));
    }

    @Operation(summary = "Create a new car", description = "Add a new car to the inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<CarResponse> createCar(@Valid @RequestBody CarRequest carRequest) {
        Car car = new Car();
        car.setMake(carRequest.getMake());
        car.setModel(carRequest.getModel());
        car.setYear(carRequest.getYear());
        car.setPrice(carRequest.getPrice());

        Car saved = carserv.createCar(car);
        return ResponseEntity.ok(new CarResponse(saved.getId(), saved.getMake(), saved.getModel(), saved.getYear(), saved.getPrice()));
    }

    @GetMapping("/paginated")
    public Page<Car> getCarsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "make") String sortBy
    ) {
        return carserv.getCarsPaginated(page, size, sortBy);
    }

    @Operation(summary = "Update a car ", description = "Update the info of a car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car info updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> updateCar(@PathVariable int id, @Valid @RequestBody CarRequest carRequest) {
        Car update = new Car();
        update.setMake(carRequest.getMake());
        update.setModel(carRequest.getModel());
        update.setYear(carRequest.getYear());
        update.setPrice(carRequest.getPrice());

        Car saved = carserv.updateCar(id, update);
        return ResponseEntity.ok(new CarResponse(saved.getId(), saved.getMake(), saved.getModel(), saved.getYear(), saved.getPrice()));
    }

    @Operation(summary = "Remove a car", description = "Delete a Car from the dealership")
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
    public ResponseEntity<?> deleteCar(@PathVariable int id) {
        carserv.getCarbyId(id); // throws exception if not found
        carserv.deleteCar(id);
        return ResponseEntity.ok("Car deleted successfully.");
    }
}
