package com.telran.phonebookapi.mapper;

import com.telran.phonebookapi.dto.MyContactDto;
import com.telran.phonebookapi.model.MyContact;
import org.springframework.stereotype.Component;

@Component
public class MyContactMapper {

    public MyContactDto mapContactToDto(MyContact contact) {
        return MyContactDto.builder().
                id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .description(contact.getDescription())
                .address(contact.getAddress())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();
    }
}
