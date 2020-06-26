package com.telran.phonebookapi.service;

import com.telran.phonebookapi.errorHandler.TokenNotFoundException;
import com.telran.phonebookapi.errorHandler.UserExistsException;
import com.telran.phonebookapi.model.ConfirmationToken;
import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.persistence.IConfirmationTokenRepository;
import com.telran.phonebookapi.errorHandler.UserDoesntExistException;
import com.telran.phonebookapi.model.RecoveryPasswordToken;
import com.telran.phonebookapi.persistence.IRecoveryPasswordToken;
import com.telran.phonebookapi.persistence.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    IRecoveryPasswordToken recoveryPasswordTokenRepository;

    @Mock
    EmailSenderService emailSenderService;

    @Mock
    BCryptPasswordEncoder encoder;

    @InjectMocks
    UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Test
    public void test_saveUser_withValidData() {
        User user = new User("mock@mail.de", "dasfsdf");

        userService.saveUser(user.getEmail(), user.getPassword());
        String tokenString = "12233";

        ConfirmationToken confirmationToken = new ConfirmationToken(user, tokenString);

        emailSenderService.sendMail(user.getEmail(), mailFrom, "conf", "link");

        verify(userRepository, times(1)).save(userArgumentCaptor.capture());

        User captorValue = userArgumentCaptor.getValue();
        assertEquals(captorValue.getEmail(), user.getEmail());

        verify(userRepository, times(1)).findById(anyString());
        verify(tokenRepository, times(1)).save(any());
        verify(encoder, times(1)).encode(anyString());

    }

    @Test
    public void test_activateUser_statusOk() {
        User user = new User("mock@mail.de", "dasfsdf");
        user.setActive(true);

        String tokenString = "12333";
        ConfirmationToken token = new ConfirmationToken(user, tokenString);

        when(tokenRepository.findById(tokenString)).thenReturn(Optional.of(token));

        userService.activateUser(tokenString);
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());

        User userValue = userArgumentCaptor.getValue();
        assertEquals(userValue.getEmail(), user.getEmail());

        verify(tokenRepository, times(1)).findById(any());
        verify(tokenRepository, times(1)).delete(any());

    }

    @Test
    public void test_saveUser_userAlreadyCreated() {

        User user = new User("mock@mail.de", "dasfsdf");

        when(userRepository.findById(user.getEmail())).thenThrow(new UserExistsException("already exists"));


        Exception userExistsException = assertThrows(UserExistsException.class, () -> userService.saveUser(user.getEmail(), user.getPassword()));
        assertEquals("already exists", userExistsException.getMessage());

    }

    @Test
    public void test_activateUser_with_TokenNotFoundException() {
        User user = new User("mock@mail.de", "dasfsdf");
        String tokenString = "12333";

        ConfirmationToken token = new ConfirmationToken(user, tokenString);

        when(tokenRepository.findById(token.getConfirmationToken())).thenThrow(new TokenNotFoundException("please register"));
        Exception exception = assertThrows(TokenNotFoundException.class, () -> userService.activateUser(tokenString));
        assertEquals("please register", exception.getMessage());
    }


    @Test
    public void testRequestRecoveryPassword_EmptyList_UserDoesntExistsException() {
        String email = "test@gmail.com";

        when(userRepository.findById(email)).thenThrow(new UserDoesntExistException("Person not found"));

        verify(userRepository, times(1)).findById(anyString());

        Exception exception = assertThrows(UserDoesntExistException.class, () -> userService.requestRecoveryPassword(email));
        assertEquals("Person not found", exception.getMessage());

    }

    @Test
    public void testRequestRecoveryPassword_UserExist_TokenGenerated() {
        User user = new User("test@gmail.com", "test");
        String generatedToken = "12345";
        RecoveryPasswordToken recoveryPasswordToken = new RecoveryPasswordToken(user, generatedToken);

        when(recoveryPasswordTokenRepository.findById(generatedToken)).thenReturn(Optional.of(recoveryPasswordToken));

        userService.requestRecoveryPassword(user.getEmail());

        emailSenderService.sendMail(user.getEmail(), mailFrom, "conf", "link");

        User userValue = userArgumentCaptor.getValue();
        assertEquals(userValue.getEmail(), user.getEmail());

        verify(userRepository, times(1)).findById(any());
        verify(tokenRepository, times(1)).save(any());
        verify(encoder, times(1)).encode(anyString());
    }

    @Test
    public void testChangePassword_UserExistsAndInvalidToken_TokenNotFoundException() {
        String token = "testToken";
        String email = "test@gmail.com";

        when(recoveryPasswordTokenRepository.findById(token)).thenThrow(new TokenNotFoundException("Please, request your link once again"));

        verify(recoveryPasswordTokenRepository, times(1)).findById(any());

        Exception exception = assertThrows(TokenNotFoundException.class, () -> userService.changePassword(token, email));
        assertEquals("Please, request your link once again", exception.getMessage());

    }

    @Test
    public void testChangePassword_UserExistsAndValidToken_Status200() {
        User user = new User("test@gmail.com", "test");
        user.setActive(true);

        String newPassword = "newTest";
        String generatedToken = "12345";

        RecoveryPasswordToken recoveryPasswordToken = new RecoveryPasswordToken(user, generatedToken);
        when(recoveryPasswordTokenRepository.findById(generatedToken)).thenReturn(Optional.of(recoveryPasswordToken));
        userService.changePassword(generatedToken, newPassword);

        verify(recoveryPasswordTokenRepository, times(1)).findById(any());
        verify(recoveryPasswordTokenRepository, times(1)).delete(any());

        User userValue = userArgumentCaptor.getValue();
        assertEquals(userValue.getPassword(), user.getPassword());

    }
}



