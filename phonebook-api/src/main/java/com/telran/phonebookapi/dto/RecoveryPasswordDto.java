package com.telran.phonebookapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RecoveryPasswordDto {

    @Size(min = 5, max = 10)
    @NotEmpty(message = "Please provide a password")
    public String password;

    private String recoveryToken;

}
