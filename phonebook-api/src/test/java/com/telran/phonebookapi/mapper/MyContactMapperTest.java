package com.telran.phonebookapi.mapper;

import com.telran.phonebookapi.dto.MyContactDto;
import com.telran.phonebookapi.model.MyContact;
import com.telran.phonebookapi.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MyContactMapperTest {

    MyContactMapper contactMapper = new MyContactMapper();

    @Test
    void ContactDto() {
        User user = new User("test@gmail.com", "112233");
        MyContact contact = new MyContact("Max", user);

        contact.setLastName("Mustermann");
        contact.setDescription("This is an example of a contact");
        contact.setAddress("Berlin, 10117, Friedrichstraße 176-179");
        contact.setPhone("017677788890");
        contact.setEmail("max.mustermann@gamil.mock");

        MyContactDto contactDto =
                new MyContactDto(1,
                        "Max",
                        "Mustermann",
                        "This is an example of a contact",
                        "Berlin, 10117, Friedrichstraße 176-179",
                        "017677788890",
                        "max.mustermann@gamil.mock"
                );

        MyContactDto contactDtoMapped = contactMapper.mapContactToDto(contact);
        assertEquals(contactDto.firstName, contactDtoMapped.firstName);
        assertEquals(contactDto.lastName, contactDtoMapped.lastName);

        assertEquals(contactDto.description, contactDtoMapped.description);

        assertEquals(contactDto.address, contactDtoMapped.address);
        assertEquals(contactDto.phone, contactDtoMapped.phone);
        assertEquals(contactDto.email, contactDtoMapped.email);
    }
}
