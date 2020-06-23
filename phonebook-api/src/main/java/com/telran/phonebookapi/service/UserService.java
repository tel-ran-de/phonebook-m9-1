package com.telran.phonebookapi.service;

import com.telran.phonebookapi.errorHandler.TokenNotFoundException;
import com.telran.phonebookapi.errorHandler.UserExistsException;
import com.telran.phonebookapi.model.ConfirmationToken;
import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.persistence.IConfirmationTokenRepository;
import com.telran.phonebookapi.persistence.IUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
public class UserService {

    private IUserRepository userRepository;
    private EmailSenderService emailSenderService;
    private IConfirmationTokenRepository confirmationTokenRepository;
    private BCryptPasswordEncoder encoder;

    public UserService(IUserRepository userRepository, EmailSenderService emailSenderService, IConfirmationTokenRepository confirmationTokenRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.emailSenderService = emailSenderService;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.encoder = encoder;
    }

    @Value("${com.telran.mail.api.host}")
    private String host;

    private final String MESSAGE = "Thank you for registration on PhoneBook Appl." +
            " Please, visit next link:" +
            host +
            "user/activation/";
    private final String SUBJ = "activation of you account";
    private final String USER_EXISTS = "User already exists";
    private final String NO_REGISTRATION = "Please, register";


    @Value("${spring.mail.username}")
    private String mailFrom;

    public void saveUser(String email, String password) {
        Optional<User> userFromDB = userRepository.findById(email);

        if (userFromDB.isPresent()) {
            throw new UserExistsException(USER_EXISTS);
        } else {  //new user
            String encodedPass = encoder.encode(password);
            User user = new User(email, encodedPass);
            userRepository.save(user);

            String tokenString = UUID.randomUUID().toString();

            ConfirmationToken token = new ConfirmationToken(user, tokenString);
            confirmationTokenRepository.save(token);

            emailSenderService.sendMail(email, mailFrom,
                    SUBJ,
                    MESSAGE + tokenString);

        }

    }


    public void activateUser(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findById(token).orElseThrow(() -> new TokenNotFoundException(NO_REGISTRATION));

        User user = confirmationToken.getUser();
        user.setActive(true);
        userRepository.save(user);

        confirmationTokenRepository.delete(confirmationToken);
    }
}
