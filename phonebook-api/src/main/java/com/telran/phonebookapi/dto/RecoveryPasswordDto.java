package com.telran.phonebookapi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Getter
public class RecoveryPasswordDto {

    @Email(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,10}$")
    @NotEmpty(message = "Please provide an e-mail")
    public String email;
}
