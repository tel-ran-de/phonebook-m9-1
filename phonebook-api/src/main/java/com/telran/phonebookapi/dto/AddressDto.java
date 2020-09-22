package com.telran.phonebookapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    public int id;

    public String city;

    public String country;

    public String street;

    public String zip;

    public int contactId;

}
