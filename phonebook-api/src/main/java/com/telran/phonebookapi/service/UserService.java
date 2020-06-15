package com.telran.phonebookapi.service;

import com.telran.phonebookapi.dto.UserDto;
import com.telran.phonebookapi.errorHandler.TokenNotFoundException;
import com.telran.phonebookapi.errorHandler.UserExistsException;
import com.telran.phonebookapi.model.ConfirmationToken;
import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.persistence.ConfirmationTokenRepository;
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
    private ConfirmationTokenRepository confirmationTokenRepository;
    private BCryptPasswordEncoder encoder;

    private final String MESSAGE="Thank you for registration on PhoneBook Appl. Please, visit next link: http://localhost:8080/confirmation?token=" ;


    public void saveUser(UserDto userToSave) {
        Optional<User> userFromDB = userRepository.findById(userToSave.getEmail());

        if (userFromDB.isPresent()) {
            throw new UserExistsException(userToSave.getEmail());
        } else {
            String encodedPass = encoder.encode(userToSave.getPassword());
            User user = new User(userToSave.getEmail(), encodedPass);
            userRepository.save(user);

            ConfirmationToken token = new ConfirmationToken(user);
            confirmationTokenRepository.save(token);


            emailSenderService.sendMail(userToSave.getEmail(),
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
