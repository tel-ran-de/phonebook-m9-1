package com.telran.phonebookapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddPhoneDto {

    @NotBlank
    @Schema(example = "+49")
    public String countryCode;
    @NotBlank
    @Schema(example = "017677788890")
    public String phoneNumber;
    @Schema(example = "1")

    public int contactId;
}
