package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        if (!userService.saveUser(user)) {
            return ResponseEntity.badRequest().body("user already exist");
        }
        return (ResponseEntity<?>) ResponseEntity.ok();
    }
}
