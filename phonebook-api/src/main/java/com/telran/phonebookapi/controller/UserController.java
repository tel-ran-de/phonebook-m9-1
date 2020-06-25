package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.dto.RecoveryPasswordDto;
import com.telran.phonebookapi.service.EmailSenderService;
import com.telran.phonebookapi.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    private final EmailSenderService emailSenderService;

    public UserController(UserService userService, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.emailSenderService = emailSenderService;
    }

    @PostMapping("password-recover")
    public void passwordRecoverRequest(@RequestBody @Valid RecoveryPasswordDto recoveryPasswordDto) {
        userService.requestRecoveryPassword(recoveryPasswordDto.getEmail());
    }

    @PostMapping("new-password")
    public void changePassword(@RequestBody @Valid RecoveryPasswordDto recoveryPasswordDto) {
        userService.changePassword(recoveryPasswordDto.password, recoveryPasswordDto.getRecoveryToken());
    }

}
