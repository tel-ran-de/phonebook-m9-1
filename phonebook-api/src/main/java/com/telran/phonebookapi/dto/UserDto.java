package com.telran.phonebookapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class UserDto {

    @Email(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,10}$")
    @NotEmpty(message = "Please provide an e-mail")
    public String email;

    @Size(min = 1, max = 10)
    @NotBlank(message = "User name cannot be empty")
    public String name;

    @Size(min = 2, max = 20)
    @NotBlank(message = "User lastname can not be empty")
    public String lastName;

    @Size(min = 5, max = 10)
    @NotEmpty(message = "Please provide a password")
    public String password;
}
