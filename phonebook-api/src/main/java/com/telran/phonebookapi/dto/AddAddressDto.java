package com.telran.phonebookapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddAddressDto {
    @ApiModelProperty(example = "Berlin")
    public String city;
    @ApiModelProperty(example = "Germany")
    public String country;
    @ApiModelProperty(example = "Friedrichstraße 176-179")
    public String street;
    @ApiModelProperty(example = "10117 ")
    public String zip;
    @ApiModelProperty(example = "1")
    public int contactId;
}
