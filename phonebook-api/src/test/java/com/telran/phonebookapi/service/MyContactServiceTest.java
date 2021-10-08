package com.telran.phonebookapi.service;

import com.telran.phonebookapi.model.MyContact;
import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.persistance.IMyContactRepository;
import com.telran.phonebookapi.persistance.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

@ExtendWith(MockitoExtension.class)
class MyContactServiceTest {

    @Mock
    IUserRepository userRepository;

    @Mock
    IMyContactRepository contactRepository;

    @InjectMocks
    MyContactService contactService;

    String firstName = "Max";
    String lastName = "Mustermann";
    String description = "This is an example of a contact";
    String address = "Berlin, 10117, Friedrichstraße 176-179";
    String phone = "017677788890";
    String email = "max.mustermann@gamil.mock";
    User user = new User("test@gmail.com", "test");

    @Test
    public void testAdd_userExists_userWithContact() {

        when(userRepository.findById(user.getEmail())).thenReturn(Optional.of(user));
        contactService.add(user.getEmail(), firstName, lastName, description, address, email, phone);

        verify(contactRepository, times(1)).save(argThat(contact ->
                contact.getFirstName().equals(firstName) &&
                        contact.getLastName().equals(lastName) &&
                        contact.getDescription().equals(description) &&
                        contact.getAddress().equals(address) &&
                        contact.getPhone().equals(phone) &&
                        contact.getEmail().equals(email) &&
                        contact.getUser().getEmail().equals(user.getEmail())));
    }

    @Test
    public void testAdd_userExistsEmailUpperCase_userWithContact() {

        String userEmailToUpperCase = user.getEmail().toUpperCase();
        when(userRepository.findById(userEmailToUpperCase)).thenReturn(Optional.of(user));
        contactService.add(userEmailToUpperCase, firstName, lastName, description, address, email, phone);

        verify(contactRepository, times(1)).save(argThat(contact ->
                contact.getFirstName().equals(firstName) &&
                        contact.getLastName().equals(lastName) &&
                        contact.getDescription().equals(description) &&
                        contact.getAddress().equals(address) &&
                        contact.getPhone().equals(phone) &&
                        contact.getEmail().equals(email) &&
                        contact.getUser().getEmail().equals(user.getEmail())));
    }

    @Test
    public void testAdd_userDoesNotExist_EntityNotFoundException() {

        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                contactService.add(user.getEmail(), firstName, lastName, description, address, email, phone));

        verify(userRepository, times(1)).findById(anyString());
        assertEquals("Error! This user doesn't exist in our DB", exception.getMessage());
    }

    @Test
    public void testEdit_contactExist_AllFieldsChanged() {

        MyContact oldContact = new MyContact("TestName", user);
        oldContact.setFirstName(firstName);
        oldContact.setLastName(lastName);
        oldContact.setDescription(description);
        oldContact.setAddress(address);
        oldContact.setEmail(email);
        oldContact.setPhone(phone);

        when(contactRepository.findById(oldContact.getId())).thenReturn(Optional.of(oldContact));

        String newFirstName = "New Max";
        String newLastName = "New Mustermann";
        String newDescription = "new This is an example of a contact";
        String newAddress = "new Berlin, 10117, Friedrichstraße 176-179";
        String newEmail = "max.mustermann@gamil.mock";
        String newPhone = "new 017677788890";
        contactService.edit(user.getEmail(), oldContact.getId(), newFirstName, newLastName, newDescription, newAddress, newEmail, newPhone);

        verify(contactRepository, times(1)).save(argThat(contact ->
                contact.getFirstName().equals(newFirstName) &&
                        contact.getLastName().equals(newLastName) &&
                        contact.getDescription().equals(newDescription) &&
                        contact.getAddress().equals(newAddress) &&
                        contact.getPhone().equals(newPhone) &&
                        contact.getEmail().equals(newEmail) &&
                        contact.getUser().getEmail().equals(user.getEmail())));
    }

    @Test
    public void testEditAny_contactDoesNotExist_EntityNotFoundException() {
        MyContact oldContact = new MyContact();

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> contactService.edit(user.getEmail(), oldContact.getId(), firstName, lastName, description, address, email, phone));

        verify(contactRepository, times(1)).findById(oldContact.getId());
        verify(contactRepository, never()).save(any());
        assertEquals("Error! This contact doesn't exist in our DB", exception.getMessage());
    }


    @Captor
    ArgumentCaptor<MyContact> contactCaptor;

    @Test
    public void testRemoveById_contactExists_ContactDeleted() {

        MyContact contact = new MyContact(firstName, user);
        contact.setLastName(lastName);
        contact.setDescription(description);
        contact.setAddress(address);
        contact.setEmail(email);
        contact.setPhone(phone);

        when(contactRepository.findById(contact.getId())).thenReturn(Optional.of(contact));

        contactService.removeById(user.getEmail(), contact.getId());

        List<MyContact> capturedContacts = contactCaptor.getAllValues();
        verify(contactRepository, times(1)).deleteById(contact.getId());
        assertEquals(0, capturedContacts.size());
    }

    @Test
    public void testRemoveById_contactDoesNotExist_EntityNotFoundException() {

        MyContact contact = new MyContact();

        Exception exception = assertThrows(EntityNotFoundException.class, () -> contactService.removeById(user.getEmail(), contact.getId()));

        verify(contactRepository, times(1)).findById(any());
        assertEquals("Error! This contact doesn't exist in our DB", exception.getMessage());
    }


    @Test
    public void testGetById_userWithContact_Contact() {

        MyContact contact = new MyContact(firstName, user);
        contact.setLastName(lastName);
        contact.setDescription(description);
        contact.setAddress(address);
        contact.setEmail(email);
        contact.setPhone(phone);

        when(contactRepository.findById(contact.getId())).thenReturn(Optional.of(contact));
        MyContact contactFounded = contactService.getById(user.getEmail(), contact.getId());

        assertEquals(contact.getFirstName(), contactFounded.getFirstName());
        assertEquals(contact.getLastName(), contactFounded.getLastName());
        assertEquals(contact.getAddress(), contactFounded.getAddress());
        assertEquals(contact.getEmail(), contactFounded.getEmail());
        assertEquals(contact.getPhone(), contactFounded.getPhone());
        assertEquals(contact.getUser().getEmail(), user.getEmail());

        verify(contactRepository, times(1)).findById(contact.getId());
    }

    @Test
    public void testGetById_wrongContactId_ContactNotExist() {

        MyContact contact = new MyContact();

        Exception exception = assertThrows(EntityNotFoundException.class, () -> contactService.getById(user.getEmail(), contact.getId()));

        verify(contactRepository, times(1)).findById(any());
        assertEquals("Error! This contact doesn't exist in our DB", exception.getMessage());
    }

    @Test
    public void testGetAllByUserId_userWithContacts_3Contacts() {
        List<MyContact> contactList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            MyContact contact = spy(new MyContact(firstName + " " + i, user));
            contact.setLastName(lastName + " " + i);
            contact.setDescription(description + " " + i);
            contact.setAddress(address + " " + i);
            contact.setEmail(email + " " + i);
            contact.setPhone(phone + " " + i);
            contactList.add(contact);
        }

        when(userRepository.findById(user.getEmail())).thenReturn(Optional.of(user));
        when(contactRepository.findAllByUser(user)).thenReturn(contactList);
        List<MyContact> contactsFound = contactService.getAllContactsByUserId(user.getEmail());

        assertEquals(contactList.get(0).getFirstName(), contactsFound.get(0).getFirstName());
        assertEquals(contactList.get(0).getLastName(), contactsFound.get(0).getLastName());
        assertEquals(contactList.get(0).getDescription(), contactsFound.get(0).getDescription());
        assertEquals(contactList.get(0).getAddress(), contactsFound.get(0).getAddress());
        assertEquals(contactList.get(0).getPhone(), contactsFound.get(0).getPhone());
        assertEquals(contactList.get(0).getEmail(), contactsFound.get(0).getEmail());

        assertEquals(contactList.get(1).getFirstName(), contactsFound.get(1).getFirstName());
        assertEquals(contactList.get(1).getLastName(), contactsFound.get(1).getLastName());
        assertEquals(contactList.get(1).getDescription(), contactsFound.get(1).getDescription());
        assertEquals(contactList.get(1).getAddress(), contactsFound.get(1).getAddress());
        assertEquals(contactList.get(1).getPhone(), contactsFound.get(1).getPhone());
        assertEquals(contactList.get(1).getEmail(), contactsFound.get(1).getEmail());

        assertEquals(contactList.get(2).getFirstName(), contactsFound.get(2).getFirstName());
        assertEquals(contactList.get(2).getLastName(), contactsFound.get(2).getLastName());
        assertEquals(contactList.get(2).getDescription(), contactsFound.get(2).getDescription());
        assertEquals(contactList.get(2).getAddress(), contactsFound.get(2).getAddress());
        assertEquals(contactList.get(2).getPhone(), contactsFound.get(2).getPhone());
        assertEquals(contactList.get(2).getEmail(), contactsFound.get(2).getEmail());

        assertEquals(contactList.size(), contactsFound.size());

        verify(userRepository, times(1)).findById(user.getEmail());
        verify(contactRepository, times(1)).findAllByUser(user);
    }

    @Test
    public void testGetAllByUserId_userWithContactsUpperCase_3Contacts() {
        List<MyContact> contactList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            MyContact contact = spy(new MyContact(firstName + " " + i, user));
            contact.setLastName(lastName + " " + i);
            contact.setDescription(description + " " + i);
            contact.setAddress(address + " " + i);
            contact.setEmail(email + " " + i);
            contact.setPhone(phone + " " + i);
            contactList.add(contact);
        }

        String userEmailToUpperCase = user.getEmail().toUpperCase();
        when(userRepository.findById(userEmailToUpperCase)).thenReturn(Optional.of(user));
        when(contactRepository.findAllByUser(user)).thenReturn(contactList);

        List<MyContact> contactsFound = contactService.getAllContactsByUserId(userEmailToUpperCase);

        assertEquals(contactList.get(0).getFirstName(), contactsFound.get(0).getFirstName());
        assertEquals(contactList.get(0).getLastName(), contactsFound.get(0).getLastName());
        assertEquals(contactList.get(0).getDescription(), contactsFound.get(0).getDescription());
        assertEquals(contactList.get(0).getAddress(), contactsFound.get(0).getAddress());
        assertEquals(contactList.get(0).getPhone(), contactsFound.get(0).getPhone());
        assertEquals(contactList.get(0).getEmail(), contactsFound.get(0).getEmail());
        assertEquals(contactList.get(0).getUser().getEmail(), user.getEmail());

        assertEquals(contactList.get(1).getFirstName(), contactsFound.get(1).getFirstName());
        assertEquals(contactList.get(1).getLastName(), contactsFound.get(1).getLastName());
        assertEquals(contactList.get(1).getDescription(), contactsFound.get(1).getDescription());
        assertEquals(contactList.get(1).getAddress(), contactsFound.get(1).getAddress());
        assertEquals(contactList.get(1).getPhone(), contactsFound.get(1).getPhone());
        assertEquals(contactList.get(1).getEmail(), contactsFound.get(1).getEmail());
        assertEquals(contactList.get(1).getUser().getEmail(), user.getEmail());

        assertEquals(contactList.get(2).getFirstName(), contactsFound.get(2).getFirstName());
        assertEquals(contactList.get(2).getLastName(), contactsFound.get(2).getLastName());
        assertEquals(contactList.get(2).getDescription(), contactsFound.get(2).getDescription());
        assertEquals(contactList.get(2).getAddress(), contactsFound.get(2).getAddress());
        assertEquals(contactList.get(2).getPhone(), contactsFound.get(2).getPhone());
        assertEquals(contactList.get(2).getEmail(), contactsFound.get(2).getEmail());
        assertEquals(contactList.get(2).getUser().getEmail(), user.getEmail());

        assertEquals(contactList.size(), contactsFound.size());

        verify(userRepository, times(1)).findById(userEmailToUpperCase);
        verify(contactRepository, times(1)).findAllByUser(user);
    }

    @Test
    public void testDeleteAllByUser_userExist_deleted() {
        when(userRepository.findById(user.getEmail())).thenReturn(Optional.of(user));
        contactService.removeAllContactsByUser(user.getEmail());
        verify(contactRepository, times(1)).deleteAllByUser(user);
    }

    @Test
    public void testDeleteAllByUser_userNotExist_deleted() {
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> contactService.getAllContactsByUserId(user.getEmail()));
        verify(contactRepository, never()).deleteAllByUser(user);
        verify(userRepository, times(1)).findById(user.getEmail());
        assertEquals("Error! This user doesn't exist in our DB", exception.getMessage());
    }

    @Test
    public void testDeleteAllByUser_userEmailToUpperCase_deleted() {
        String userEmailToUpperCase = user.getEmail().toUpperCase();
        when(userRepository.findById(userEmailToUpperCase)).thenReturn(Optional.of(user));
        contactService.removeAllContactsByUser(userEmailToUpperCase);
        verify(contactRepository, times(1)).deleteAllByUser(user);
    }
}
