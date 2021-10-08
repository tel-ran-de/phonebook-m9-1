package com.telran.phonebookapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class AddMyContactDto {
    @Schema(example = "Max")
    public String firstName;

    @Schema(example = "Mustermann")
    public String lastName;

    @Schema(example = "This is an example of a contact")
    public String description;

    @Schema(example = "Berlin, 10117, Friedrichstraße 176-179")
    public String address;

    @Schema(example = "017677788890")
    public String phone;

    @Schema(example = "max.mustermann@gamil.mock")
    public String email;
}
