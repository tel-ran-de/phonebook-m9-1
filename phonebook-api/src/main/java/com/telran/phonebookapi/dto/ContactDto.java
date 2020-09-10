package com.telran.phonebookapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactDto {

    public int id;
    @Getter
    public String firstName;

    public String lastName;

    public String description;
}
