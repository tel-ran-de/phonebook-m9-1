package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.dto.*;
import com.telran.phonebookapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.stream.Collectors;

@Tag(name = "User")
@RestController
@RequestMapping("/api/user")
public class UserController {

    UserService userService;

    @Value("${com.telran.testUser.testEmail}")
    String testEmail;
    @Value("${com.telran.testUser.testPassword}")
    String testPassword;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get JWT of test user",
            description = "This token will expire on 07 October 2036" +
                    "\n\nTest user email: ${com.telran.testUser.testEmail}")
    @GetMapping("/token")
    public String getTestUserToken() {
        return "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6InRlc3RAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImV4cCI6MjEwNjk3ODI5Nn0.GM1wsoRV2QoAsD6wKmIk7N49DDpuCejK4BC9H9YItJvesH5vft8HO2uqTPnGQJwJ5oXKS2OILqP1yoanMnIMkA";
    }

    @Operation(summary = "New user registration",
            description = "An email will be sent with a link to activation to the address specified during registration. The link will contain an activation token" +
                    "\n\nEmail validation pattern: ^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,10}$")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public String addUser(@Parameter(description = "user registration body") @Valid @RequestBody AddUserDto registerUser) {
        return userService.addUser(registerUser.email, registerUser.password);
    }

    @Operation(summary = "Activate registered user",
            description = "As a parameter, you need to specify the token that was sent to the email of the registered user")
    @GetMapping("/activation/{token}")
    public void activateUser(@NotBlank @Parameter(description = "password recovery token") @PathVariable String token) {
        userService.activateUser(token);
    }

    @Operation(summary = "Password recovery request",
            description = "Unauthorized user request for updating the password")
    @PostMapping("/password/recovery")
    public void recoverPassword(@Valid @RequestBody RecoveryPasswordDto recoveryPasswordUserEmail) {
        userService.sendRecoveryToken(recoveryPasswordUserEmail.email);
    }

    @Operation(summary = "Update password",
            description = "Updating the password. The recovery token is contained within the request body")
    @PutMapping("/password")
    public void changePassword(@Valid @RequestBody NewPasswordDto newPasswordDto) {
        userService.createNewPassword(newPasswordDto.token, newPasswordDto.password);
    }

    @Operation(summary = "Get the data of the authenticated user", security = {@SecurityRequirement(name = "JWT")},
            description = "Returns the data of the logged in user." +
                    "\n\nUser data(needs user email) for updating is contained in the JWT")
    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public UserDto getUser(Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return UserDto.builder()
                .roles(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .email(userDetails.getUsername())
                .build();
    }

    @Operation(summary = "Updating the password of registered user", security = {@SecurityRequirement(name = "JWT")},
            description = "Updating the password of registered user. " +
                    "\n\nUser data(needs user email) for updating is contained in the JWT")
    @PutMapping("/auth-password")
    @PreAuthorize("isAuthenticated()")
    public void changePasswordAuth(Authentication auth, @Valid @RequestBody NewPasswordAuthDto newPasswordAuthDto) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        userService.changePasswordAuth(email, newPasswordAuthDto.password);
    }
}
