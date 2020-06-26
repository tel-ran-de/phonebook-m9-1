package com.telran.phonebookapi.service;

import com.telran.phonebookapi.errorHandler.TokenNotFoundException;
import com.telran.phonebookapi.errorHandler.UserDoesntExistException;
import com.telran.phonebookapi.errorHandler.UserExistsException;
import com.telran.phonebookapi.model.ConfirmationToken;
import com.telran.phonebookapi.model.RecoveryPasswordToken;
import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.persistence.IConfirmationTokenRepository;
import com.telran.phonebookapi.persistence.IRecoveryPasswordToken;
import com.telran.phonebookapi.persistence.IUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final IUserRepository userRepository;
    private final EmailSenderService emailSenderService;
    private final IConfirmationTokenRepository confirmationTokenRepository;
    private final BCryptPasswordEncoder encoder;
    private final IRecoveryPasswordToken recoveryPasswordTokenRepo;

    public UserService(IUserRepository userRepository, EmailSenderService emailSenderService, IConfirmationTokenRepository confirmationTokenRepository, BCryptPasswordEncoder encoder, IRecoveryPasswordToken recoveryPasswordTokenRepo) {
        this.userRepository = userRepository;
        this.emailSenderService = emailSenderService;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.encoder = encoder;
        this.recoveryPasswordTokenRepo = recoveryPasswordTokenRepo;
    }

    @Value("${com.telran.phonebook.ui.host}")
    String uiHost;

    private final String REGISTRATION_MESSAGE = "Thank you for registration on PhoneBook Appl." +
            " Please, visit the following link: %s" +
            "user/activation/%s";
    static final String SUBJ = "activation of you account";
    static final String USER_EXISTS = "User already exists";
    static final String NO_REGISTRATION = "Please, register";
    final String MESSAGE_RECOVER_PASSWORD_REQUEST = "You have requested the recovery password option." +
            " Please, visit next link: %s/user/new-password/%s";
    static final String RECOVERY_PASSWORD = "Recovery password";
    static final String USER_DOESNT_EXIST = "Person not found";
    static final String INVALID_TOKEN = "Please, request your link once again";

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

            String message = String.format(REGISTRATION_MESSAGE, uiHost, tokenString);
            emailSenderService.sendMail(email, mailFrom,
                    SUBJ,
                    message);
        }
    }

    public void activateUser(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findById(token).orElseThrow(() -> new TokenNotFoundException(NO_REGISTRATION));

        User user = confirmationToken.getUser();
        user.setActive(true);
        userRepository.save(user);

        confirmationTokenRepository.delete(confirmationToken);
    }

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void requestRecoveryPassword(String email) {
        User user = userRepository.findById(email).orElseThrow(() -> new UserDoesntExistException(USER_DOESNT_EXIST));

        String generatedToken = generateToken();

        RecoveryPasswordToken recoveryToken = new RecoveryPasswordToken(user, generatedToken);
        recoveryPasswordTokenRepo.save(recoveryToken);

        String message = String.format(MESSAGE_RECOVER_PASSWORD_REQUEST, uiHost, generatedToken);
        emailSenderService.sendMail(email, mailFrom, RECOVERY_PASSWORD, message);
    }

    public void changePassword(String token, String password) {
        RecoveryPasswordToken tokenFromDB = recoveryPasswordTokenRepo.findById(token).orElseThrow(() -> new TokenNotFoundException(INVALID_TOKEN));

        User user = tokenFromDB.getUser();
        String encodedPass = encoder.encode(password);
        user.setPassword(encodedPass);
        userRepository.save(user);
        recoveryPasswordTokenRepo.delete(tokenFromDB);
    }
}
