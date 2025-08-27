package com.example.demo.Controllers;

import com.example.demo.DTOs.CarResponse;
import com.example.demo.DTOs.UserRequest;
import com.example.demo.DTOs.UserResponse;
import com.example.demo.Services.UserServices;
import com.example.demo.entity.Car;
import com.example.demo.entity.User;
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


@Tag(name = "User", description = "User Management APIs")
@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth") // applies to all endpoints in this controller
public class UserController {

    @Autowired
    private UserServices userService;

    // ✅ Helper to convert entity → response
    private UserResponse mapToResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }

@Operation(summary = "Get all Users", description = "Retrieve all the user in a list")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User list retrieved successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CarResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Unauthorized",
                content = @Content)
})
@GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponse> responseList = users.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

@Operation(summary = "Get a User", description = "Retrieve a specific User")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CarResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Unauthorized",
                content = @Content)
})
@GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(mapToResponse(user));
    }

@Operation(summary = "Add a new User", description = "Create a new user to the system")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "New User added  successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CarResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Unauthorized",
                content = @Content)
})
@PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        User newUser = new User();
        newUser.setUsername(userRequest.getUsername());
        newUser.setPassword(userRequest.getPassword());
        newUser.setRole(userRequest.getRole());

        User createdUser = userService.createUser(newUser);
        return ResponseEntity.ok(mapToResponse(createdUser));
    }

    @GetMapping("/paginated")
    public Page<User> getUsersPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "make") String sortBy
    ) {
        return userService.getUserPaginated(page, size, sortBy);
    }

@Operation(summary = "Update the info of a User", description = "Change certain value or info in a User")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User info updated successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CarResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Unauthorized",
                content = @Content)
})
@PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable int id, @Valid @RequestBody UserRequest userRequest) {
        User updatedUser = new User();
        updatedUser.setUsername(userRequest.getUsername());
        updatedUser.setPassword(userRequest.getPassword());
        updatedUser.setRole(userRequest.getRole());

        User savedUser = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(mapToResponse(savedUser));
    }

@Operation(summary = "Delete a User", description = "Remove a User from the system")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User deleted successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CarResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Unauthorized",
                content = @Content)
})
@DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }
}
