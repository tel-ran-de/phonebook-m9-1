package com.telran.phonebookapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactDto {

    @ApiModelProperty(example = "1", notes = "need for edit contact and for response")
    public int id;

    @ApiModelProperty(example = "Max")
    @NotBlank
    public String firstName;
    @ApiModelProperty(example = "Mustermann")
    public String lastName;
    @ApiModelProperty(example = "This is example contact")
    public String description;
}
