package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.dto.ChangePasswordDto;
import com.telran.phonebookapi.dto.UserDto;
import com.telran.phonebookapi.dto.RecoveryPasswordDto;
import com.telran.phonebookapi.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public void addUser(@RequestBody @Valid UserDto userDto) {
        userService.saveUser(userDto.getEmail(), userDto.getPassword());
    }

    @GetMapping("/activation/{token}")
    public void emailConfirmation(@PathVariable String token) {
        userService.activateUser(token);
    }

    @PostMapping("/password-recover")
    public void recoverPasswordRequest(@RequestBody @Valid RecoveryPasswordDto recoveryPasswordDto) {
        userService.requestRecoveryPassword(recoveryPasswordDto.getEmail());
    }

    @PostMapping("/new-password")
    public void changePassword(@RequestBody @Valid ChangePasswordDto changePasswordDto) {
        userService.changePassword(changePasswordDto.recoveryToken, changePasswordDto.password);
    }

}
