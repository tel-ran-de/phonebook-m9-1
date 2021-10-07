package com.telran.phonebookapi.dto;

import lombok.AllArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@AllArgsConstructor
@ApiIgnore
public class ErrorDto {

    public String message;
}
