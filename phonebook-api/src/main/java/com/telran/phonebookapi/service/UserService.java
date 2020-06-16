package com.telran.phonebookapi.service;

import com.telran.phonebookapi.errorHandler.TokenNotFoundException;
import com.telran.phonebookapi.errorHandler.UserExistsException;
import com.telran.phonebookapi.model.ConfirmationToken;
import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.persistence.IConfirmationTokenRepository;
import com.telran.phonebookapi.persistence.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class UserService {

    private IUserRepository userRepository;
    private EmailSenderService emailSenderService;
    private IConfirmationTokenRepository confirmationTokenRepository;
    private BCryptPasswordEncoder encoder;

    private final String MESSAGE="Thank you for registration on PhoneBook Appl. Please, visit next link: http://localhost:8080/confirmation?token=" ;


    public void saveUser(String email, String password) {
        Optional<User> userFromDB = userRepository.findById(email);

        if (userFromDB.isPresent()) {
            throw new UserExistsException(email);
        } else {
            String encodedPass = encoder.encode(password);
            User user = new User(email, encodedPass);
            userRepository.save(user);

            ConfirmationToken token = new ConfirmationToken(user);
            confirmationTokenRepository.save(token);


            emailSenderService.sendMail(email,
                    "tel-ran@gmail.com",
                    "mail confirmation",
                    String.format(MESSAGE + token.getConfirmationToken()));

        }

    }


    public void activateUser(String token) {
        ConfirmationToken tokenFromDB = confirmationTokenRepository.findByConfirmationToken(token);

        if (tokenFromDB == null) {
            throw new TokenNotFoundException();
        }
        User user = tokenFromDB.getUser();
        user.setActive(true);
        userRepository.save(user);

        confirmationTokenRepository.deleteById(tokenFromDB.getId());


    }
}
