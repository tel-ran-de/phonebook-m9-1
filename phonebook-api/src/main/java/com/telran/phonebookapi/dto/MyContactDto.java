package com.telran.phonebookapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyContactDto {
    public int id;
    public String firstName;
    public String lastName;
    public String description;
    public String address;
    public String phone;
    public String email;
}
