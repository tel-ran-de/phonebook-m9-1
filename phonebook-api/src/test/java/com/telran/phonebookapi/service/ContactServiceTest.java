package com.telran.phonebookapi.service;

import com.telran.phonebookapi.dto.ContactDto;
import com.telran.phonebookapi.model.Contact;
import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.persistance.IContactRepository;
import com.telran.phonebookapi.persistance.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    IUserRepository userRepository;

    @Mock
    IContactRepository contactRepository;

    @InjectMocks
    ContactService contactService;

  //  @Spy
   // ContactMapper contactMapper;

    @Test
    public void testAdd_userExists_userWithContact() {

        User user = new User("test@gmail.com", "test");
        when(userRepository.findById(user.getEmail())).thenReturn(Optional.of(user));

        ContactDto contactDto = new ContactDto();
        contactDto.firstName = "ContactName";
        contactDto.userId = user.getEmail();
        contactDto.lastName = "Nachname";
        contactDto.description = "person";
        contactService.add(contactDto.firstName, contactDto.lastName, contactDto.description, contactDto.userId);

        verify(contactRepository, times(1)).save(any());
        verify(contactRepository, times(1)).save(argThat(contact ->
                contact.getFirstName().equals(contactDto.firstName)  &&  contact.getLastName().equals(contactDto.lastName)
                        &&  contact.getDescription().equals(contactDto.description) && contact.getUser().getEmail().equals(contactDto.userId)
        ));
    }


    @Test
    public void testAdd_userDoesNotExist_EntityNotFoundException() {

        ContactDto contactDto = new ContactDto();
        contactDto.firstName = "ContactName";
        contactDto.userId = "wrong@gmail.com";

        Exception exception = assertThrows(EntityNotFoundException.class, () -> contactService.add(contactDto.firstName, contactDto.lastName, contactDto.description, contactDto.userId));

        verify(userRepository, times(1)).findById(anyString());
        assertEquals("Error! This user doesn't exist in our DB", exception.getMessage());
    }

/*
    @Test
    public void testEditAllFields_contactExist_AllFieldsChanged() {

        User user = new User("test@gmail.com", "test");

        Contact oldContact = new Contact("TestName", user);
        ContactDto contactDto = new ContactDto();
        contactDto.firstName = "NewName";
        contactDto.lastName = "NewLastName";
        contactDto.description = "newDescription";
        contactDto.userId = user.getEmail();

        when(contactRepository.findById(contactDto.id)).thenReturn(Optional.of(oldContact));

        contactService.editAllFields(contactDto);

        verify(contactRepository, times(1)).save(any());
        verify(contactRepository, times(1)).save(argThat(contact ->
                contact.getFirstName().equals(contactDto.firstName) && contact.getLastName().equals(contactDto.lastName) && contact.getDescription().equals(contactDto.description)
                        && contact.getUser().getEmail().equals(contactDto.userId)
        ));
    }

    @Test
    public void testEditAny_contactDoesNotExist_EntityNotFoundException() {

        ContactDto contactDto = new ContactDto();
        contactDto.firstName = "ContactName";
        contactDto.lastName = "LastName";
        contactDto.description = "Description";
        contactDto.userId = "wrong@gmail.com";

        Exception exception = assertThrows(EntityNotFoundException.class, () -> contactService.editAllFields(contactDto));

        verify(contactRepository, times(1)).findById(any());
        assertEquals("Error! This contact doesn't exist in our DB", exception.getMessage());
    }

    @Captor
    ArgumentCaptor<Contact> contactCaptor;

    @Test
    public void testRemoveById_contactExists_ContactDeleted() {

        User user = new User("test@gmail.com", "test");
        Contact contact = new Contact("Name", user);
        contact.setLastName("Surname");
        contact.setDescription("person");

        ContactDto contactDto = new ContactDto(1, "Name", "Surname", "person", "test@gmail.com");

        when(contactRepository.findById(contactDto.id)).thenReturn(Optional.of(contact));
        contactService.removeById(contactDto.id);

        List<Contact> capturedContacts = contactCaptor.getAllValues();
        verify(contactRepository, times(1)).deleteById(contactDto.id);
        assertEquals(0, capturedContacts.size());
    }
*/
    @Test
    public void testGetById_userWithContact_Contact() {
        User user = new User("test@gmail.com", "test");
        Contact contact = new Contact("Name", user);
        contact.setLastName("Surname");
        contact.setDescription("person");

        ContactDto contactDto = new ContactDto(1, "Name", "Surname", "person", "test@gmail.com");

        when(contactRepository.findById(contactDto.id)).thenReturn(Optional.of(contact));
        Contact contactFounded = contactService.getById(contactDto.id);

        assertEquals(contactDto.firstName, contactFounded.getFirstName());
        assertEquals(contactDto.lastName, contactFounded.getLastName());
        assertEquals(contactDto.description, contactFounded.getDescription());

        verify(contactRepository, times(1)).findById(argThat(
                id -> id == contactDto.id));
    }
    @Test
    public void testGetAllByUserId_userWithContacts_3Contacts() {
        User user = new User("test@gmail.com", "test");
        Contact contact1 = new Contact("Name1", user);
        Contact contact2 = new Contact("Name2", user);
        Contact contact3 = new Contact("Name3", user);

        contact1.setLastName("Surname1");
        contact1.setDescription("person");
        contact2.setLastName("Surname2");
        contact2.setDescription("person");
        contact3.setLastName("Surname3");
        contact3.setDescription("person");

        ContactDto contactDto1 = new ContactDto(1, "Name1", "Surname1", "person", "test@gmail.com");
        ContactDto contactDto2 = new ContactDto(2, "Name2", "Surname2", "person", "test@gmail.com");
        ContactDto contactDto3 = new ContactDto(3, "Name3", "Surname3", "person", "test@gmail.com");

        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact1);
        contacts.add(contact2);
        contacts.add(contact3);

        when(contactRepository.findAllByUserEmail(user.getEmail())).thenReturn(contacts);
        List<Contact> contactsFound = contactService.getAllContactsByUserId(user.getEmail());

        assertEquals(contactDto1.firstName, contactsFound.get(0).getFirstName());
        assertEquals(contactDto1.lastName, contactsFound.get(0).getLastName());
        assertEquals(contactDto1.description, contactsFound.get(0).getDescription());

        assertEquals(contactDto2.firstName, contactsFound.get(1).getFirstName());
        assertEquals(contactDto2.lastName, contactsFound.get(1).getLastName());
        assertEquals(contactDto2.description, contactsFound.get(1).getDescription());

        assertEquals(contactDto3.firstName, contactsFound.get(2).getFirstName());
        assertEquals(contactDto3.lastName, contactsFound.get(2).getLastName());
        assertEquals(contactDto3.description, contactsFound.get(2).getDescription());

        verify(contactRepository, times(1)).findAllByUserEmail(argThat(
                email -> email.equals(user.getEmail())));
    }
}
