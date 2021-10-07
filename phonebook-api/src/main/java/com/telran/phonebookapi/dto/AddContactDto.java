package com.telran.phonebookapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddContactDto {
    @Schema(example = "Max")
    @NotBlank
    public String firstName;
    @Schema(example = "Mustermann")
    public String lastName;
    @Schema(example = "This is example contact")
    public String description;
}
