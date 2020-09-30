package com.telran.phonebookapi.config;

import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.persistance.IUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Profile(value = "dev")
@Component
public class TestUserRunner implements ApplicationRunner {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Value("${com.telran.testUser.testEmail}")
    String testEmail;
    @Value("${com.telran.testUser.testPassword}")
    String testPassword;
    final
    IUserRepository userRepository;

    public TestUserRunner(IUserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.findById(testEmail).isEmpty()) {
            
            User user = new User(testEmail, bCryptPasswordEncoder.encode(testPassword));
            user.setActive(true);
            userRepository.save(user);
        }
    }
}
