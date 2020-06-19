package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.dto.UserDto;
import com.telran.phonebookapi.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    private final EmailSenderService emailSenderService;

    public UserController(UserService userService, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.emailSenderService = emailSenderService;
    }


    @PostMapping("/registration")
    public void addUser(@RequestBody  @Valid UserDto userDto)
    {
        userService.saveUser(userDto.getEmail(), userDto.getPassword());
    }

    @GetMapping("/activation/{token}")
    public void emailConfirmation(@PathVariable String token){
        userService.activateUser(token);
    }
}
