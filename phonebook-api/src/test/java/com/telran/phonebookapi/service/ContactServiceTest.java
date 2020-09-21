package com.telran.phonebookapi.service;

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

    @Test
    public void testAdd_userExists_userWithContact() {

        User user = new User("test@gmail.com", "test");
        when(userRepository.findById(user.getEmail())).thenReturn(Optional.of(user));

        String firstName = "ContactName";
        String lastName = "Nachname";
        String description = "person";
        contactService.add(firstName, lastName, description, user.getEmail());

        verify(contactRepository, times(1)).save(any());
        verify(contactRepository, times(1)).save(argThat(contact ->
                contact.getFirstName().equals(firstName) && contact.getLastName().equals(lastName)
                        && contact.getDescription().equals(description) && contact.getUser().getEmail().equals(user.getEmail())
        ));
    }


    @Test
    public void testAdd_userDoesNotExist_EntityNotFoundException() {

        User user = new User("test@gmail.com", "test");
        String firstName = "ContactName";
        String lastName = "Nachname";
        String description = "person";

        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                contactService.add(firstName, lastName, description, user.getEmail()+"s"));

        verify(userRepository, times(1)).findById(anyString());
        assertEquals("Error! This user doesn't exist in our DB", exception.getMessage());
    }

    @Test
    public void testEdit_contactExist_AllFieldsChanged() {

        User user = new User("test@gmail.com", "test");

        Contact oldContact = new Contact("TestName", user);
        String firstName = "ContactName";
        String lastName = "Nachname";
        String description = "person";

        when(contactRepository.findById(oldContact.getId())).thenReturn(Optional.of(oldContact));
        contactService.edit(oldContact.getId(), firstName, lastName, description);

        verify(contactRepository, times(1)).save(any());
        verify(contactRepository, times(1)).save(argThat(contact ->
                contact.getFirstName().equals(oldContact.getFirstName()) && contact.getLastName().equals(oldContact.getLastName())
                        && contact.getDescription().equals(oldContact.getDescription())
                        && contact.getUser().getEmail().equals(oldContact.getUser().getEmail())
        ));
    }

    @Test
    public void testEditAny_contactDoesNotExist_EntityNotFoundException() {
        Contact oldContact = new Contact();

        String firstName = "ContactName";
        String lastName = "Nachname";
        String description = "person";

        Exception exception = assertThrows(EntityNotFoundException.class, () -> contactService.edit(oldContact.getId()+1, firstName, lastName, description));

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

        when(contactRepository.findById(contact.getId())).thenReturn(Optional.of(contact));
        contactService.removeById(contact.getId());

        List<Contact> capturedContacts = contactCaptor.getAllValues();
        verify(contactRepository, times(1)).deleteById(contact.getId());
        assertEquals(0, capturedContacts.size());
    }

    @Test
    public void testRemoveById_contactDoesNotExist_EntityNotFoundException() {

        Contact contact = new Contact();

        Exception exception = assertThrows(EntityNotFoundException.class, () -> contactService.removeById(contact.getId()+1));

        verify(contactRepository, times(1)).findById(any());
        assertEquals("Error! This contact doesn't exist in our DB", exception.getMessage());
    }


    @Test
    public void testGetById_userWithContact_Contact() {
        User user = new User("test@gmail.com", "test");
        Contact contact = new Contact("Name", user);
        contact.setLastName("Surname");
        contact.setDescription("person");

        when(contactRepository.findById(contact.getId())).thenReturn(Optional.of(contact));
        Contact contactFounded = contactService.getById(contact.getId());

        assertEquals(contact.getFirstName(), contactFounded.getFirstName());
        assertEquals(contact.getLastName(), contactFounded.getLastName());
        assertEquals(contact.getDescription(), contactFounded.getDescription());

        verify(contactRepository, times(1)).findById(argThat(
                id -> id == contact.getId()));
    }

    @Test
    public void testGetById_wrongContactId_ContactNotExist() {

        Contact contact = new Contact();

        Exception exception = assertThrows(EntityNotFoundException.class, () -> contactService.getById(contact.getId()+1));

        verify(contactRepository, times(1)).findById(any());
        assertEquals("Error! This contact doesn't exist in our DB", exception.getMessage());
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

        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact1);
        contacts.add(contact2);
        contacts.add(contact3);

        when(contactRepository.findAllByUserEmail(user.getEmail())).thenReturn(contacts);
        List<Contact> contactsFound = contactService.getAllContactsByUserId(user.getEmail());

        assertEquals(contact1.getFirstName(), contactsFound.get(0).getFirstName());
        assertEquals(contact1.getLastName(), contactsFound.get(0).getLastName());
        assertEquals(contact1.getDescription(), contactsFound.get(0).getDescription());

        assertEquals(contact2.getFirstName(), contactsFound.get(1).getFirstName());
        assertEquals(contact2.getLastName(), contactsFound.get(1).getLastName());
        assertEquals(contact2.getDescription(), contactsFound.get(1).getDescription());

        assertEquals(contact3.getFirstName(), contactsFound.get(2).getFirstName());
        assertEquals(contact3.getLastName(), contactsFound.get(2).getLastName());
        assertEquals(contact3.getDescription(), contactsFound.get(2).getDescription());

        verify(contactRepository, times(1)).findAllByUserEmail(argThat(
                email -> email.equals(user.getEmail())));
    }

    @Test
    public void testGetProfile_Profile() {
        User user = new User("test@gmail.com", "test");
        Contact myProfile = new Contact("Name", user);
        myProfile.setLastName("Surname");
        myProfile.setDescription("person");
        user.setMyProfile(myProfile);

        when(contactRepository.findById(user.getMyProfile().getId())).thenReturn(Optional.of(myProfile));
        Contact contactFounded = contactService.getById(myProfile.getId());

        assertEquals(myProfile.getFirstName(), contactFounded.getFirstName());
        assertEquals(myProfile.getLastName(), contactFounded.getLastName());
        assertEquals(myProfile.getDescription(), contactFounded.getDescription());

        verify(contactRepository, times(1)).findById(argThat(
                id -> id == myProfile.getId()));
    }

    @Test
    public void testEditProfile_AllFieldsChanged() {

        User user = new User("test@gmail.com", "test");
        String email = "test@gmail.com";
        Contact myProfile = new Contact();
        user.setMyProfile(myProfile);

        String firstName = "ContactName";
        String lastName = "Nachname";
        String description = "person";

        when(userRepository.findById(email)).thenReturn(Optional.of(user));
        contactService.editProfile(user.getEmail(), firstName, lastName, description);

        verify(contactRepository, times(1)).save(any());
        verify(contactRepository, times(1)).save(argThat(contact ->
                contact.getFirstName().equals(myProfile.getFirstName()) && contact.getLastName().equals(myProfile.getLastName())
                        && contact.getDescription().equals(myProfile.getDescription())
        ));
    }
}
