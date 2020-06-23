package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.dto.UserDto;
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
    public void passwordRecoverRequest(@RequestBody @Valid UserDto userDto) {
        userService.requestRecoveryPassword(userDto.getEmail());
    }

    @PostMapping("new-password/{token}")
    public void changePassword (@PathVariable String token, @RequestBody @Valid UserDto userDto) {
        userService.changePassword(token, userDto.password);
    }

}
