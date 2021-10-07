package com.telran.phonebookapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddUserDto {

    @Email(message = "Please, check entered email is correct",
            regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,10}$")
    @NotBlank
    @ApiModelProperty(example = "valid.email@email.com", notes = "used validation pattern: ^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,10}$")
    public String email;

    @Size(max = 20, min = 8, message = "The password is shorter than {min} or longer than {max}")
    @NotBlank
    @ApiModelProperty(example = "valid_password", notes = "The password is shorter than 20 or longer than 20")
    public String password;
}
