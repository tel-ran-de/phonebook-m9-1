package com.telran.phonebookapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
public class NewPasswordAuthDto {

    @NotBlank
    @Size(max = 20, min = 8, message = "The password is shorter than {min} or longer than {max}")
    @ApiModelProperty(example = "valid_password", notes = "The password is shorter than 20 or longer than 20")
    public String password;
}

