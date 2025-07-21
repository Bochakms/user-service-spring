package io.github.Bochakms.controller;

import io.github.Bochakms.assembler.UserModelAssembler;
import io.github.Bochakms.dto.UserRequest;
import io.github.Bochakms.dto.UserResponse;
import io.github.Bochakms.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Endpoints for managing users")
public class UserController {
    private final UserService userService;
    private final UserModelAssembler userModelAssembler;

    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user with the provided details")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EntityModel<UserResponse>> createUser(
            @Parameter(description = "User data to create", required = true)
            @Valid @RequestBody UserRequest userRequest) {
        
        UserResponse createdUser = userService.createUser(userRequest);
        EntityModel<UserResponse> resource = userModelAssembler.toModel(createdUser);
        
        return ResponseEntity
                .created(linkTo(methodOn(UserController.class).getUserById(createdUser.getId())).toUri())
                .body(resource);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Returns a single user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public EntityModel<UserResponse> getUserById(
            @Parameter(description = "ID of the user to retrieve", required = true)
            @PathVariable Long id) {
        
        UserResponse user = userService.getUserById(id);
        return userModelAssembler.toModel(user);
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Returns a list of all users")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users")
    public CollectionModel<EntityModel<UserResponse>> getAllUsers() {
        List<EntityModel<UserResponse>> users = userService.getAllUsers().stream()
                .map(userModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(users,
                linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel(),
                linkTo(methodOn(UserController.class).createUser(null)).withRel("create"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates an existing user's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public EntityModel<UserResponse> updateUser(
            @Parameter(description = "ID of the user to update", required = true)
            @PathVariable Long id,
            
            @Parameter(description = "Updated user data", required = true)
            @Valid @RequestBody UserRequest userRequest) {
        
        UserResponse updatedUser = userService.updateUser(id, userRequest);
        return userModelAssembler.toModel(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to delete", required = true)
            @PathVariable Long id) {
        
        userService.deleteUser(id);
        return ResponseEntity.noContent()
                .header("Link", linkTo(methodOn(UserController.class).getAllUsers()).withRel("users").toString())
                .build();
    }
}