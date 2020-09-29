package com.telran.phonebookapi.config;

import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.persistance.IUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile(value = "dev")
@Component
public class TestUserRunner implements ApplicationRunner {
    @Value("${com.telran.testUser.testEmail}")
    String testEmail;
    @Value("${com.telran.testUser.testPassword}")
    String testPassword;
    final
    IUserRepository userRepository;

    public TestUserRunner(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void run(ApplicationArguments args) {

       User user = new User(testEmail, testPassword);
       user.setActive(true);
       userRepository.save(user);
    }
}
