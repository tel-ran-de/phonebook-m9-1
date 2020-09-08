package com.telran.phonebookapi.mapper;

import com.telran.phonebookapi.dto.ContactDto;
import com.telran.phonebookapi.model.Contact;
import com.telran.phonebookapi.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ContactMapperTest {

    ContactMapper contactMapper = new ContactMapper();

    @Test
    void ContactDto() {
        User user = new User("test@gmail.com", "112233");
        Contact contact = new Contact("Name", user);
        contact.setDescription("Description");

        ContactDto contactDto = new ContactDto(1, "Name", "LastName", "Description");

        ContactDto contactDtoMapped = contactMapper.mapContactToDto(contact);
        assertEquals(contactDto.firstName, contactDtoMapped.firstName);
        assertEquals(contactDto.description, contactDtoMapped.description);
    }
}
