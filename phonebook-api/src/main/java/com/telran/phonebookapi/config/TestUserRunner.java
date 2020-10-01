package com.telran.phonebookapi.config;

import com.telran.phonebookapi.model.Contact;
import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.model.UserRole;
import com.telran.phonebookapi.persistance.IContactRepository;
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
    private final
    IUserRepository userRepository;
    IContactRepository contactRepository;

    public TestUserRunner(IUserRepository userRepository, IContactRepository contactRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.findById(testEmail).isEmpty()) {

            User user = new User(testEmail, bCryptPasswordEncoder.encode(testPassword));
            user.setActive(true);
            user.addRole(UserRole.USER);
            Contact profile = new Contact();
            user.setMyProfile(profile);
            contactRepository.save(profile);
            userRepository.save(user);
            profile.setUser(user);
        }
    }
}
