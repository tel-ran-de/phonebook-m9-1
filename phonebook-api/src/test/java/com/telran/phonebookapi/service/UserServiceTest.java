package com.telran.phonebookapi.service;

import com.telran.phonebookapi.exception.TokenNotFoundException;
import com.telran.phonebookapi.exception.UserAlreadyExistsException;
import com.telran.phonebookapi.model.ActivationToken;
import com.telran.phonebookapi.model.RecoveryToken;
import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.persistance.IActivationTokenRepository;
import com.telran.phonebookapi.persistance.IContactRepository;
import com.telran.phonebookapi.persistance.IRecoveryTokenRepository;
import com.telran.phonebookapi.persistance.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    IUserRepository userRepository;

    @Mock
    IRecoveryTokenRepository recoveryTokenRepository;

    @Mock
    IActivationTokenRepository activationTokenRepository;

    @Mock
    EmailSender emailSender;

    @Mock
    IContactRepository contactRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    UserService userService;

    @BeforeEach
    public void init() {
        lenient().doAnswer(invocation -> invocation.getArgument(0)).when(bCryptPasswordEncoder).encode(anyString());
    }

    @Test
    public void testAdd_user_passesToRepo() {
        String email = "test@gmail.com";
        String password = "1111111";

        userService.addUser(email, bCryptPasswordEncoder.encode(password));

        verify(userRepository, times(1)).save(any());
        verify(userRepository, times(1)).save(argThat(user ->
                user.getEmail().equals(email)
                        && user.getPassword().equals(bCryptPasswordEncoder.encode(password)
                )));
        verify(contactRepository, times(1)).save(any());

        verify(activationTokenRepository, times(1)).save(any());
        verify(activationTokenRepository, times(1)).save(argThat(token ->
                token.getUser().getEmail().equals(email)
        ));
        verify(emailSender, times(1)).sendMail(eq(email),
                eq(UserService.ACTIVATION_SUBJECT),
                anyString());
    }

    @Test
    public void testAdd_saveUserUpperCase_repoSavedLowerCase() {
        String email = "IVANOV@gmail.com";
        String password = "pass";

        userService.addUser(email, password);

        verify(userRepository, times(1)).save(argThat(user ->
                user.getPassword().equals(password) && user.getEmail().equals(email.toLowerCase())
        ));
    }

    @Test
    public void testAdd_userExist_UserAlreadyExistsException() {
        User user = new User("test@gmail.com", "1111111");
        String email = "test@gmail.com";
        String password = "1111111";

        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        Exception exception = assertThrows(UserAlreadyExistsException.class, () -> userService.addUser(email, password));

        verify(userRepository, times(1)).findById(any());
        assertEquals("Error! User already exists", exception.getMessage());
    }

    @Test
    public void testActivateUser_activeStatusChanged() {
        User ourUser = new User("johndoe@mail.com", "1234");
        String token = UUID.randomUUID().toString();
        ActivationToken activationToken = new ActivationToken(token, ourUser);

        when(activationTokenRepository.findById(token)).thenReturn(Optional.of(activationToken));

        userService.activateUser(token);

        assertTrue(ourUser.isActive());

        verify(userRepository, times(1)).save(any());
        verify(userRepository, times(1)).save(argThat(user -> user.getEmail().equals(ourUser.getEmail()) &&
                user.isActive()));

        verify(activationTokenRepository, times(1)).findById(token);
    }

    @Test
    public void testActivateUser_notActiveLink_TokenNotFoundException() {
        String token = UUID.randomUUID().toString();

        Exception exception = assertThrows(TokenNotFoundException.class, () -> userService.activateUser(token));

        verify(activationTokenRepository, times(1)).findById(token);
        assertEquals("Your link is not active anymore", exception.getMessage());
    }

    @Test
    public void testSendRecoveryToken_tokenIsSavedToRepo() {
        String email = "johndoe@mail.com";
        User ourUser = new User(email, "1234");

        when(userRepository.findById(email)).thenReturn(Optional.of(ourUser));

        userService.sendRecoveryToken(email);

        verify(recoveryTokenRepository, times(1)).save(any());

        verify(recoveryTokenRepository, times(1)).save(argThat(token ->
                token.getUser().getEmail().equals(email)));

        verify(emailSender, times(1)).sendMail(eq(email), anyString(), anyString());
    }

    @Test
    public void testSendRecoveryToken_userUpperCase_tokenIsSavedToRepo() {
        String email = "IVANOV@gmail.com";
        User ourUser = new User(email.toLowerCase(), "1234");
        when(userRepository.findById(email.toLowerCase())).thenReturn(Optional.of(ourUser));

        userService.sendRecoveryToken(email);

        verify(recoveryTokenRepository, times(1)).save(any());

        verify(recoveryTokenRepository, times(1)).save(argThat(token ->
                token.getUser().getEmail().equals(email.toLowerCase())));

        verify(emailSender, times(1)).sendMail(eq(email.toLowerCase()), anyString(), anyString());
    }

    @Test
    public void testCreateNewPassword_newPasswordIsSaved() {
        User ourUser = new User("johndoe@mail.com", "1234");
        String token = UUID.randomUUID().toString();
        RecoveryToken recoveryToken = new RecoveryToken(token, ourUser);

        when(recoveryTokenRepository.findById(token)).thenReturn(Optional.of(recoveryToken));

        userService.createNewPassword(token, "4321");

        verify(userRepository, times(1)).save(any());

        verify(userRepository, times(1)).save(argThat(user ->
                user.getPassword().equals("4321")));

        verify(recoveryTokenRepository, times(1)).findById(token);
    }

    @Test
    public void testCreateNewPassword_notActiveLink_TokenNotFoundException() {
        String password = "111111";
        String token = UUID.randomUUID().toString();

        Exception exception = assertThrows(TokenNotFoundException.class, () -> userService.createNewPassword(token, password));

        verify(recoveryTokenRepository, times(1)).findById(token);
        assertEquals("Your link is not active anymore", exception.getMessage());
    }

    @Test
    public void testGetUserByEmail_userExist() {
        String email = "johndoe@mail.com";
        User ourUser = new User("johndoe@mail.com", "1234");

        when(userRepository.findById(ourUser.getEmail())).thenReturn(Optional.of(ourUser));

        User user = userService.getUserByEmail(email);

        verify(userRepository, times(1)).findById(email);

        assertEquals(user.getEmail(), ourUser.getEmail());
        assertEquals(user.getPassword(), ourUser.getPassword());
    }

    @Test
    public void testGetUserByEmail_userNotExist() {
        String email = "johndoe@mail.com";

        Exception exception = assertThrows(EntityNotFoundException.class, () -> userService.getUserByEmail(email));

        verify(userRepository, times(1)).findById(email);
        assertEquals("Error! This user doesn't exist in our DB", exception.getMessage());
    }

    @Test
    public void testChangePasswordAuth_newPasswordIsSaved() {

        User ourUser = new User("johndoe@mail.com", "12345678");
        ourUser.setActive(true);

        when(userRepository.findById("johndoe@mail.com")).thenReturn(Optional.of(ourUser));
        userService.changePasswordAuth("johndoe@mail.com", "123456790");

        verify(userRepository, times(1)).save(any());

        verify(userRepository, times(1)).save(argThat(user ->
                user.getPassword().equals("123456790")));
    }
}

