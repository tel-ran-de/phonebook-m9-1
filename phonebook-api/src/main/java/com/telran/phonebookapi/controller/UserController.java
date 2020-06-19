package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.dto.UserDto;
import com.telran.phonebookapi.service.EmailSenderService;
import com.telran.phonebookapi.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final EmailSenderService emailSenderService;

    public UserController(UserService userService, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.emailSenderService = emailSenderService;
    }

    @PostMapping("/password-recover")
    public void passwordRecover(@RequestBody @Valid UserDto userDto) {
        userService.findUser(userDto.getEmail());
    }

    @GetMapping("/new-password/{token}")
    public void emailConfirmation (@PathVariable String token, @RequestParam String password) {
        userService.changePassword(token, password);
    }

}
