package com.telran.phonebookapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
public class NewPasswordDto {
    @NotBlank
    @Size(max = 20, min = 8, message = "The password is shorter than {min} or longer than {max}")
    @ApiModelProperty(example = "valid_password", notes = "The password is shorter than 20 or longer than 20")
    public String password;

    @NotBlank
    @Size(max = 100, min = 10)
    @ApiModelProperty(example = "", notes = "this token was sent, saved to the database and sent to the email inside the link when requesting to restore the password")
    public String token;
}
