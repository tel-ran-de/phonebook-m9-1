package com.telran.phonebookapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddAddressDto {

    @Schema(example = "Berlin")
    public String city;
    @Schema(example = "Germany")
    public String country;
    @Schema(example = "Friedrichstraße 176-179")
    public String street;
    @Schema(example = "10117 ")
    public String zip;
    @Schema(example = "1")
    public int contactId;
}
