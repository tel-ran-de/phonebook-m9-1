package com.telran.phonebookapi.service;

import com.telran.phonebookapi.controller.TokenNotFoundException;
import com.telran.phonebookapi.controller.UserDoesntExistException;
import com.telran.phonebookapi.dto.UserDto;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
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
        when(userRepository.findById(email)).thenThrow(UserDoesntExistException.class);
    }

    @Captor
    ArgumentCaptor<User> userCaptor;

    @Test
    public void testChangePassword_UserExistsAndInvalidToken_TokenNotFoundException() {
        UserDto userIn = new UserDto("test@gmail.com", "test");

        userService.save(userIn.email, userIn.password);

        verify(userRepository).save(userCaptor.capture());
        User user = new User("test@gmail.com", "test");

        RecoveryPasswordToken token = new RecoveryPasswordToken(user);
        when(recoveryPasswordTokenRepository.findById(token.getRecoveryPasswordToken())).thenThrow(TokenNotFoundException.class);
        assertThrows(TokenNotFoundException.class, () -> recoveryPasswordTokenRepository.findById(token.getRecoveryPasswordToken()));
    }

    @Test
    public void testChangePassword_UserExistsAndValidToken_Status200() {

        UserDto userIn = new UserDto("test@gmail.com", "test");

        String password = "test";
        String token = "testToken";
        RecoveryPasswordToken tokenRecovery = new RecoveryPasswordToken(user);

        when(recoveryPasswordTokenRepository.findById(tokenRecovery.getRecoveryPasswordToken()).thenReturn(Optional.of(token)));
        userService.changePassword(token, password);

        verify(recoveryPasswordTokenRepository, times(1)).findById(any());
        verify(recoveryPasswordTokenRepository, times(1)).delete(any());

        UserDto userFounded = userService.findById(userIn.email);

    }

}
