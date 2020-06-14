package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.errorHandler.ControllerErrors;
import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;
    @Autowired
     private ControllerErrors controllerErrors;

    @PostMapping("/registration")
    public ResponseEntity<?> addUser(@RequestBody  @Valid User user, BindingResult bindingResult) {
        if(user.getPassword()!=null && !user.getPassword2().equals(user.getPassword2())){
            bindingResult.reject("email", "incorrect password");
            return ResponseEntity.badRequest().body("Passwords are different");
        }
        if(bindingResult.hasErrors()){
            Map<String, String> errors = controllerErrors.getErrors(bindingResult);
            return ResponseEntity.badRequest().body(errors);
        }
        if (!userService.saveUser(user)) {
            return ResponseEntity.badRequest().body("user already exist");
        }
        return (ResponseEntity<?>) ResponseEntity.ok();
    }
}
