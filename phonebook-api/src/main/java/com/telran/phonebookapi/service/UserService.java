package com.telran.phonebookapi.service;

import com.telran.phonebookapi.model.ActivationToken;
import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.persistence.IActivationToken;
import com.telran.phonebookapi.persistence.IUserRepository;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private IUserRepository userRepository;
    private EmailSenderService emailSenderService;
    private IActivationToken activationToken;
    private BCryptPasswordEncoder encoder;

    private final String MESSAGE = "You have requested the recovery password option. Please, visit next link: http://localhost:8080/new-password?token=";
    private final String SUBJ="Recovery password";

    @Value("${spring.mail.username}")
    private String mailFrom;

    public void findUser(String email) {
        Optional<User> userFromDB = userRepository.findById(email);

        if (userFromDB.isPresent()) {
            User foundedUser = userFromDB.get();
            ActivationToken token = new ActivationToken(foundedUser);
            activationToken.save(token);
            emailSenderService.sendMail(email, mailFrom, SUBJ, MESSAGE + token.getActivationToken());
        } else {
            // throw new UserDoesntExistException(email);
        }
    }

    public void changePassword(String token, String password) {
        Optional<ActivationToken> tokenFromDB = activationToken.findById(token);

        if (tokenFromDB.isPresent()) {
            ActivationToken recoveryToken = tokenFromDB.get();
            User user = recoveryToken.getUser();
            String encodedPass = encoder.encode(password);
            user.setPassword(encodedPass);
            user.setActive(true);
            userRepository.save(user);
            activationToken.deleteById(recoveryToken.getActivationToken());
        } else {
            // throw new TokenNotFoundException;
        }

    }

}
