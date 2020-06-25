package com.telran.phonebookapi.service;

import com.telran.phonebookapi.errorHandler.TokenNotFoundException;
import com.telran.phonebookapi.errorHandler.UserDoesntExistException;
import com.telran.phonebookapi.model.RecoveryPasswordToken;
import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.persistence.IRecoveryPasswordToken;
import com.telran.phonebookapi.persistence.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IRecoveryPasswordToken recoveryPasswordTokenRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testRequestRecoveryPassword_EmptyList_UserDoesntExistsException() {
        String email = "test@gmail.com";
        userService.requestRecoveryPassword(email);
        when(userRepository.findById(email)).thenThrow(new UserDoesntExistException("Person not found"));

        verify(userRepository, times(1)).findById(any());

        Exception exception = assertThrows(UserDoesntExistException.class, () -> userService.requestRecoveryPassword(email));
        assertEquals("Person not found", exception.getMessage());

    }

    @Captor
    ArgumentCaptor<User> userCaptor;

    @Test
    public void testRequestRecoveryPassword_UserExist_TokenGenerated() {
        User user = new User("test@gmail.com", "test");
        user.setActive(true);

        String generatedToken = "12345";

        RecoveryPasswordToken recoveryPasswordToken = new RecoveryPasswordToken(user, generatedToken);

        when(recoveryPasswordTokenRepository.findById(generatedToken)).thenReturn(Optional.of(recoveryPasswordToken));
        userService.requestRecoveryPassword(user.getEmail());

        verify(userRepository, times(1)).findById(any());

        User userValue = userCaptor.getValue();
        assertEquals(userValue.getEmail(), user.getEmail());

        verify(userRepository, times(1)).findById(any());

    }

    @Test
    public void testChangePassword_UserExistsAndInvalidToken_TokenNotFoundException() {
        String token = "testToken";
        String email = "test@gmail.com";
        userService.changePassword(token, email);
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

        User userValue = userCaptor.getValue();
        assertEquals(userValue.getPassword(), user.getPassword());

    }

}
