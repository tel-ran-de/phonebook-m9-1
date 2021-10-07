package com.telran.phonebookapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddPhoneDto {

    @NotBlank
    @ApiModelProperty(example = "+49")
    public String countryCode;
    @NotBlank
    @ApiModelProperty(example = "017677788890")
    public String phoneNumber;
    @ApiModelProperty(example = "1")

    public int contactId;
}
