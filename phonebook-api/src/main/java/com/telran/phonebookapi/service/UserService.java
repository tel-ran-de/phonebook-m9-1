package com.telran.phonebookapi.service;

import com.telran.phonebookapi.controller.TokenNotFoundException;
import com.telran.phonebookapi.controller.UserDoesntExistException;
import com.telran.phonebookapi.model.RecoveryPasswordToken;
import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.persistence.IRecoveryPasswordToken;
import com.telran.phonebookapi.persistence.IUserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private IUserRepository userRepository;
    private EmailSenderService emailSenderService;
    private IRecoveryPasswordToken recoveryPasswordTokenRepo;
    private BCryptPasswordEncoder encoder;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Value("${com.telran.mail.api.host}")
    private String host;

    private final String MESSAGE_RECOVER_PASSWORD_REQUEST = "You have requested the recovery password option." + " Please, visit next link:" + host + "/user/new-password?token=";
    private final String RECOVERY_PASSWORD = "Recovery password";
    public static final String USER_DOESNT_EXISTS = "Person not found";
    public static final String INVALID_TOKEN = "Please, request your link once again";

    public UserService(IUserRepository userRepository, EmailSenderService emailSenderService, IRecoveryPasswordToken recoveryPasswordTokenRepo, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.emailSenderService = emailSenderService;
        this.recoveryPasswordTokenRepo = recoveryPasswordTokenRepo;
        this.encoder = encoder;
    }

    public void requestRecoveryPassword(String email) {
        Optional<User> userFromDB = Optional.ofNullable(userRepository.findById(email).orElseThrow(() -> new UserDoesntExistException(USER_DOESNT_EXISTS)));

        User foundedUser = userFromDB.get();
        RecoveryPasswordToken token = new RecoveryPasswordToken(foundedUser);
        recoveryPasswordTokenRepo.save(token);
        emailSenderService.sendMail(email, mailFrom, RECOVERY_PASSWORD, MESSAGE_RECOVER_PASSWORD_REQUEST + token.getRecoveryPasswordToken());

    }

    public void changePassword(String token, String password) {
        Optional<RecoveryPasswordToken> tokenFromDB = Optional.ofNullable(recoveryPasswordTokenRepo.findById(token).orElseThrow(() -> new TokenNotFoundException(INVALID_TOKEN)));

        RecoveryPasswordToken recoveryPasswordToken = tokenFromDB.get();
        User user = recoveryPasswordToken.getUser();
        String encodedPass = encoder.encode(password);
        user.setPassword(encodedPass);
        userRepository.save(user);
        recoveryPasswordTokenRepo.delete(tokenFromDB.get());

    }
}
