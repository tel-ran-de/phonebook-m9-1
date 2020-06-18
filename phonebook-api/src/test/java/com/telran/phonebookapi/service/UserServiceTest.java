package com.telran.phonebookapi.service;

import com.telran.phonebookapi.errorHandler.TokenNotFoundException;
import com.telran.phonebookapi.errorHandler.UserExistsException;
import com.telran.phonebookapi.model.ConfirmationToken;
import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.persistence.IConfirmationTokenRepository;
import com.telran.phonebookapi.persistence.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    IUserRepository userRepository;
    @Mock
    IConfirmationTokenRepository tokenRepository;

    @Mock
    EmailSenderService emailSenderService;

    @InjectMocks
    UserService userService;
    @Value("${spring.mail.username}")
    private String mailFrom;


    @Test
    public void test_saveUser_withValidData(){
        User user=new User("mock@mail.de","dasfsdf");

        userRepository.save(user);

        ConfirmationToken confirmationToken=new ConfirmationToken(user);
        tokenRepository.save(confirmationToken);
        emailSenderService.sendMail(user.getEmail(), mailFrom, "conf","link");

        verify(userRepository, times(1)).save(user);
        verify(tokenRepository,times(1)).save(any());
    }

    @Test
    public void test_activateUser_statusOk(){
        User user=new User("mock@mail.de","dasfsdf");
        user.setActive(true);
        ConfirmationToken token=new ConfirmationToken(user);


        when(tokenRepository.findById(token.getConfirmationToken())).thenReturn(Optional.of(token));

        Optional<ConfirmationToken> tokenFromDB = tokenRepository.findById(token.getConfirmationToken());
        userRepository.save(user);

        tokenRepository.deleteById(tokenFromDB.get().getConfirmationToken());

        verify(userRepository,times(1)).save(user);
        verify(tokenRepository,times(1)).findById(any());
        verify(tokenRepository,times(1)).deleteById(any());

    }


    @Test
    public void test_saveUser_userAlreadyCreated(){

        User user=new User("mock@mail.de","dasfsdf");

        when(userRepository.findById(user.getEmail())).thenThrow(UserExistsException.class);

        assertThrows(UserExistsException.class, ()->userRepository.findById(user.getEmail()));

    }
    @Test
    public void test_activateUser_with_TokenNotFoundException(){
        User user=new User("mock@mail.de","dasfsdf");

        ConfirmationToken token=new ConfirmationToken(user);

        when(tokenRepository.findById(token.getConfirmationToken())).thenThrow(TokenNotFoundException.class);
        assertThrows(TokenNotFoundException.class,()->tokenRepository.findById(token.getConfirmationToken()));

    }

}
