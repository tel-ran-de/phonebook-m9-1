package com.telran.phonebookapi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
public class UserDto {

    @Email(regexp ="^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,}$" )
    @NotEmpty(message = "Please provide an e-mail")
    public String email;

    @Size(min=5, max=10)
    public String password;

}
