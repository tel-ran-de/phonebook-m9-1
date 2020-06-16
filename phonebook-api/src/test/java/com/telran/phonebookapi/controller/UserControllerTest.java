package com.telran.phonebookapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telran.phonebookapi.dto.UserDto;
import com.telran.phonebookapi.model.ConfirmationToken;
import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.persistence.IConfirmationTokenRepository;
import com.telran.phonebookapi.persistence.IUserRepository;
import com.telran.phonebookapi.service.EmailSenderService;
import com.telran.phonebookapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest

public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IUserRepository userRepository;
    @MockBean
    IConfirmationTokenRepository tokenRepository;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @MockBean
    EmailSenderService emailSenderService;


    @Test
    public void test_registration_valid() throws Exception{

        UserDto userDto = new UserDto("mock@mail.de","edqwfdsd");
        mockMvc.perform(
                post("/user/registration")
                .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

    }
    @Test
    public void test_registration_invalid_email() throws Exception{
        UserDto userDto = new UserDto("mock@mailde","edqwfdsd");

        mockMvc.perform(
                post("/user/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());

        Optional<User> byId = userRepository.findById(userDto.getEmail());
        assertThat(byId.isPresent()).isFalse();

    }

    @Test
    public void test_registration_invalid_password_toShort() throws Exception{
        UserDto userDto = new UserDto("mock@mail.de","fd");

        mockMvc.perform(
                post("/user/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());

        Optional<User> byId = userRepository.findById(userDto.getEmail());
        assertThat(byId.isPresent()).isFalse();
    }
    @Test
    public void test_registration_invalid_password_null() throws Exception{
        UserDto userDto = new UserDto("mock@mail.de",null);

        mockMvc.perform(
                post("/user/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());

        Optional<User> byId = userRepository.findById(userDto.getEmail());
        assertThat(byId.isPresent()).isFalse();
    }
    @Test
    public void test_registration_with_existing_email () throws Exception{
        UserDto userDto = new UserDto("mock@mail.de","edqwfdsd");

        UserDto userDto2 = new UserDto("mock@mail.de","gfdtttfdsd");

        mockMvc.perform(
                post("/user/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        Optional<User> userFromDb1 = userRepository.findById(userDto.getEmail());

        assertThat(userFromDb1.isPresent()).isTrue();

        mockMvc.perform(
                post("/user/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto2)))
                .andExpect(status().isOk());

        Optional<User> fromDb2 = userRepository.findById(userDto2.getEmail());
        fromDb2.get().setActive(true);
        assertThat(fromDb2.isPresent()).isTrue();

        mockMvc.perform(
                post("/user/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_confirmation() throws Exception{
        User user=new User("mock@mail.de", "fhdsjkfs");
        ConfirmationToken token=new ConfirmationToken(user);
        userRepository.save(user);
        tokenRepository.save(token);

        mockMvc.perform(
                get("/user/confirmation?token={token}",token.getConfirmationToken()))
                .andExpect(status().isOk());

        Optional<User> byId = userRepository.findById(user.getEmail());
        assertThat(user).isEqualTo(byId);
    }
}
