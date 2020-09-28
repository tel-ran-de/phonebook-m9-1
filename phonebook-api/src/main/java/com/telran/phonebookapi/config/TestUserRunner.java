package com.telran.phonebookapi.config;

import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class TestUserRunner implements ApplicationRunner {
    @Value("${com.telran.testUser.testEmail}")
    String testEmail;
    @Value("${com.telran.testUser.testPassword}")
    String testPassword;
    final
    UserService userService;

    public TestUserRunner(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(ApplicationArguments args) {

      userService.addUser(testEmail, testPassword);
    }
}
