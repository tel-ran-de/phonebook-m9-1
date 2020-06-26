package com.telran.phonebookapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telran.phonebookapi.dto.RecoveryPasswordDto;
import com.telran.phonebookapi.dto.UserDto;
import com.telran.phonebookapi.errorHandler.TokenNotFoundException;
import com.telran.phonebookapi.errorHandler.UserDoesntExistException;
import com.telran.phonebookapi.errorHandler.UserExistsException;
import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.service.EmailSenderService;
import com.telran.phonebookapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)

public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @MockBean
    EmailSenderService emailSenderService;

    @Test
    public void test_registration_valid() throws Exception {
        UserDto userDto = new UserDto("mock@mail.de", "edqwfdsd");

        mockMvc.perform(
                post("/api/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void test_registration_invalid_email() throws Exception {
        UserDto userDto = new UserDto("mock@mailde", "edqwfdsd");

        mockMvc.perform(
                post("/api/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_registration_invalid_password_toShort() throws Exception {
        UserDto userDto = new UserDto("mock@mail.de", "fd");

        mockMvc.perform(
                post("/api/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_registration_invalid_password_null() throws Exception {
        UserDto userDto = new UserDto("mock@mail.de", null);

        mockMvc.perform(
                post("/api/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void test_registration_with_existing_email() throws Exception {
        UserDto userDto = new UserDto("mock@mail.de", "edqwfdsd");

        UserDto userDto2 = new UserDto("mock@mail.de", "gfdtttfdsd");

        doThrow(UserExistsException.class).when(userService).saveUser(userDto2.getEmail(), userDto2.getPassword());

        mockMvc.perform(
                post("/api/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        mockMvc.perform(
                post("/api/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto2)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_confirmation() throws Exception {
        User user = new User("mock@mail.de", "fhdsjkfs");
        user.setActive(true);
        String tokenString = "1233";

        mockMvc.perform(
                get("/api/user/activation/{token}", tokenString))

                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testPasswordRecoverRequest_userExists_valid() throws Exception {
        UserDto userDto = new UserDto("test@mail.de", "112233");

        mockMvc.perform(
                post("/api/user/password-recover")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testPasswordRecoverRequest_userDoesntExist_invalid() throws Exception {
        UserDto userDto = new UserDto("test@mailde", "112233");

        doThrow(UserDoesntExistException.class).when(userService).requestRecoveryPassword(userDto.getEmail());

        mockMvc.perform(
                post("/api/user/password-recover")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testChangePassword_TokenExists_PasswordChanged() throws Exception {
        String newPassword = "112233";
        String generatedToken = "445566";

        RecoveryPasswordDto recoveryPasswordDto = new RecoveryPasswordDto(generatedToken, newPassword);

        mockMvc.perform(
                post("/api/user/new-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recoveryPasswordDto)))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testChangePassword_TokenNotFound_Invalid() throws Exception {
        String newPassword = "112233";
        String generatedToken = "445566";

        RecoveryPasswordDto recoveryPasswordDto = new RecoveryPasswordDto(generatedToken, newPassword);

        doThrow(TokenNotFoundException.class).when(userService).changePassword(recoveryPasswordDto.getRecoveryToken(), recoveryPasswordDto.getPassword());

        mockMvc.perform(
                post("/api/user/new-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recoveryPasswordDto)))
                .andExpect(status().isNotFound());
    }

}

