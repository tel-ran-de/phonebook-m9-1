package com.telran.phonebookapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class ChangePasswordDto {

    @NotBlank
    public String recoveryToken;

    @NotBlank
    @Size(min = 5, max = 10)
    public String password;
}
