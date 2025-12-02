package de.seuhd.campuscoffee.api.controller;

import de.seuhd.campuscoffee.api.dtos.UserDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import static de.seuhd.campuscoffee.api.util.ControllerUtils.getLocation;

import java.util.List;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.eclipse.persistence.jpa.rs.exceptions.ErrorResponse;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import de.seuhd.campuscoffee.api.mapper.UserDtoMapper;
import de.seuhd.campuscoffee.domain.ports.UserService;


/**
 * Controller for handling POS-related API requests.
 */
@Tag(name = "Users", description = "Operations related to user management.")
@Controller
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    //TODO: Implement user controller
    private final UserService userService;
    private final UserDtoMapper userDtoMapper;

    @Operation(
        summary = "Get all Users.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "array", implementation = UserDto.class)
                ),
                description = "All Users as a JSON array."
            )
        }
    )
    @GetMapping("")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(
            userService.getAll().stream()
                .map(userDtoMapper::fromDomain)
                .toList)
        );
    }

    @Operation(
        summary = "Get a User by ID.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class)
                ),
                description = "The User with the specified ID."
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User not found."
            )
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
        @PathVariable Long id) {
        return ResponseEntity.ok(
            userDtoMapper.fromDomain(
                userService.getById(id)
            )
        );
    }

    @Operation(
        summary = "Get User by login name.",
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = UserDto.class)
                        ),
                        description = "The User with the specified login name."
                ),
                @ApiResponse(
                        responseCode = "404",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponse.class)
                        ),
                        description = "User not found."
                ),
        }
)

    @GetMapping("/filter")
    public ResponseEntity<UserDto> filter(
        @RequestParam("loginName") String loginName) {
        return ResponseEntity.ok(
            userDtoMapper.fromDomain(
                userService.getByLoginName(loginName)
            )
        );
    }

    @Operation(
        summary = "Create a new User.",
        responses = {
            @ApiResponse(
                responseCode = "201",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class)
                ),
                description = "The created User."
            ),
            @ApiResponse(
                responseCode = "400",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
                ),
                description = "Invalid input data."
            )
        }
    )
    @PostMapping("")
    public ResponseEntity<UserDto> create(
        @RequestBody UserDto userDto) {

        UserDto created = upsert(userDto);
        return ResponseEntity
            .created(getLocation(created.id()))
            .body(created);
    }
