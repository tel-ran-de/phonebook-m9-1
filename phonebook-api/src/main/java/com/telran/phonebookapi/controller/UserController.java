package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.dto.*;
import com.telran.phonebookapi.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@Api(tags = "User API")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Register user", authorizations = {@Authorization(value = "JWT")}, tags = {"registration user"},
            notes = "New user registration." +
                    "An email will be sent with a link to activation to the address specified during registration. The link will contain an activation token" +
                    "\n\nEmail validation pattern: ^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,10}$" +
                    "\nno authentication needed")
    @PostMapping("")
    public void addUser(@ApiParam(value = "user registration body") @Valid @RequestBody AddUserDto registerUser) {
        userService.addUser(registerUser.email, registerUser.password);
    }

    @ApiOperation(value = "Activate registered user", authorizations = {@Authorization(value = "JWT")}, tags = {"registration user"},
            notes = "As a parameter, you need to specify the token that was sent to the email of the registered user" +
                    "\nno authentication needed")
    @GetMapping("/activation/{token}")
    @ResponseStatus(HttpStatus.CREATED)
    public void activateUser(@NotBlank @ApiParam(value = "password recovery token") @PathVariable String token) {
        userService.activateUser(token);
    }

    @ApiOperation(value = "password recovery request", authorizations = {@Authorization(value = "JWT")}, tags = {"password recovery"},
            notes = "unauthorized user request for updating the password" +
                    "\nno authentication needed")
    @PostMapping("/password/recovery")
    public void recoverPassword(@Valid @RequestBody RecoveryPasswordDto recoveryPasswordUserEmail) {
        userService.sendRecoveryToken(recoveryPasswordUserEmail.email);
    }

    @ApiOperation(value = "update password", authorizations = {@Authorization(value = "")}, tags = {"password recovery"},
            notes = "Updating the password." +
                    "\nThe recovery token is contained within the request body" +
                    "\nno authentication needed"
    )
    @PutMapping("/password")
    public void changePassword(@Valid @RequestBody NewPasswordDto newPasswordDto) {
        userService.createNewPassword(newPasswordDto.token, newPasswordDto.password);
    }

    @ApiOperation(value = "get the data of the authenticated user", authorizations = {@Authorization(value = "JWT")}, tags = {"user data"},
            notes = "returns the data of the logged in user" +
                    "\nUser data is contained in the JWT" +
                    "\nneed authentication")
    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public UserDto getUser(Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return UserDto.builder()
                .roles(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .email(userDetails.getUsername())
                .build();
    }

    @ApiOperation(value = "updating the password of registered user", authorizations = {@Authorization(value = "JWT")}, tags = {"password recovery"},
            notes = "updating the password of registered user" +
                    "\nUser data for updating is contained in the JWT" +
                    "\nneed authentication")
    @PutMapping("/auth-password")
    @PreAuthorize("isAuthenticated()")
    public void changePasswordAuth(Authentication auth, @Valid @RequestBody NewPasswordAuthDto newPasswordAuthDto) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        userService.changePasswordAuth(email, newPasswordAuthDto.password);
    }
}
