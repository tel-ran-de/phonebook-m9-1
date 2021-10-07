package com.telran.phonebookapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @Email(message = "Please, check entered email is correct",
            regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,10}$")
    @NotBlank
    @Schema(example = "valid.email@email.com", description = "used validation pattern: ^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,10}$")
    public String email;

    @Size(max = 20, min = 8, message = "The password is shorter than {min} or longer than {max}")
    @NotBlank
    @Schema(example = "valid_password", description = "The password is shorter than 20 or longer than 20")
    public String password;
    @Schema(example = "ROLE_USER", description = "ROLE_USER")
    public List<String> roles;
}
