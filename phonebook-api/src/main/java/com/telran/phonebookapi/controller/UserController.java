package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.dto.UserDto;
import com.telran.phonebookapi.service.EmailSenderService;
import com.telran.phonebookapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailSenderService emailSenderService;

    @PostMapping("/registration")
    public void addUser(@RequestBody  @Valid UserDto userDto) {
        userService.saveUser(userDto);
    }

    @GetMapping("/confirmation/{token}")
    public void emailConfirmation(@PathVariable String token) {
        userService.activateUser(token);
    }
}
