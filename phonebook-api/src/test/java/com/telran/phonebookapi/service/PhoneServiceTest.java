package com.telran.phonebookapi.service;

import com.telran.phonebookapi.model.Contact;
import com.telran.phonebookapi.model.Phone;
import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.persistance.IContactRepository;
import com.telran.phonebookapi.persistance.IPhoneRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PhoneServiceTest {

    @Mock
    IContactRepository contactRepository;

    @Mock
    IPhoneRepository phoneRepository;

    @InjectMocks
    PhoneService phoneService;

    @Test
    public void testAdd_contactExists_contactWithPhoneNumber() {
        User user = new User("test@gmail.com", "test");
        Contact contact = new Contact("Name", user);

        when(contactRepository.findById(contact.getId())).thenReturn(Optional.of(contact));

        Phone phone = new Phone("49", "12345", contact);

        phoneService.add(phone.getCountryCode(), phone.getPhoneNumber(), phone.getContact().getId());

        verify(phoneRepository, times(1)).save(any());
        verify(phoneRepository, times(1)).save(argThat(phone1 -> phone1.getPhoneNumber().equals(phone.getPhoneNumber())
                && phone1.getContact().getId() == phone.getContact().getId())
        );
    }

    @Test
    public void testAdd_contactDoesNotExist_EntityNotFoundException() {

        User user = new User("test@gmail.com", "test");
        Contact contact = new Contact("Name", user);

        Phone phone = new Phone("49", "12345", contact);

        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                phoneService.add(phone.getCountryCode(), phone.getPhoneNumber(), phone.getContact().getId()+1));

        verify(contactRepository, times(1)).findById(any());
        assertEquals("Error! This contact doesn't exist in our DB", exception.getMessage());
    }

    @Test
    public void testEditAllFields_phoneExist_AllFieldsChanged() {

        User user = new User("test@gmail.com", "test");

        Contact oldContact = new Contact("TestName", user);
        Phone oldPhone = new Phone(oldContact);

        String phoneNumber = "12345678";
        String countryCode = "49";

        phoneService.edit(oldPhone, countryCode, phoneNumber);

        verify(phoneRepository, times(1)).save(any());
        verify(phoneRepository, times(1)).save(argThat(phone ->
                phone.getPhoneNumber().equals(phoneNumber) && phone.getCountryCode().equals(countryCode)
                        && phone.getContact().getId() == oldPhone.getId())
        );
    }

    @Captor
    ArgumentCaptor<Phone> phoneArgumentCaptor;

    @Test
    public void testRemoveById_phoneExists_PhoneDeleted() {

        User user = new User("test@gmail.com", "test");

        Contact contact = new Contact("TestName", user);
        Phone phone = new Phone("+49", "12345678", contact);

        phoneService.removeById(phone.getId());

        List<Phone> capturedAddresses = phoneArgumentCaptor.getAllValues();
        verify(phoneRepository, times(1)).deleteById(phone.getId());
        assertEquals(0, capturedAddresses.size());
    }

    @Test
    public void testGetById_contactWithPhone_PhoneNumber() {

        User user = new User("test@gmail.com", "test");
        Contact contact = new Contact("TestName", user);
        Phone phone = new Phone("+49", "12345678", contact);

        when(phoneRepository.findById(phone.getId())).thenReturn(Optional.of(phone));
        Phone phoneFounded = phoneService.getById(phone.getId());

        assertEquals(phone.getCountryCode(), phoneFounded.getCountryCode());
        assertEquals(phone.getPhoneNumber(), phoneFounded.getPhoneNumber());

        verify(phoneRepository, times(1)).findById(argThat(
                id -> id == phone.getId()));
    }

    @Test
    public void testGetById_wrongPhoneId_PhoneNotExist() {

       Phone phone = new Phone();

        Exception exception = assertThrows(EntityNotFoundException.class, () -> phoneService.getById(phone.getId()));

        verify(phoneRepository, times(1)).findById(any());
        assertEquals("Error! This phone number doesn't exist in our DB", exception.getMessage());
    }

    @Test
    public void testGetAllByContactId_ContactWithPhones_3Phones() {
        User user = new User("test@gmail.com", "test");
        Contact contact = new Contact("Name1", user);

        Phone phone1 = new Phone("+49", "12345678", contact);
        Phone phone2 = new Phone("+49", "12345688", contact);
        Phone phone3 = new Phone("+49", "12345677", contact);

        List<Phone> phones = new ArrayList<>();
        phones.add(phone1);
        phones.add(phone2);
        phones.add(phone3);

        when(phoneRepository.findAllByContactId(contact.getId())).thenReturn(phones);
        List<Phone> phonesFound = phoneService.getAllPhoneNumbersByContactId(contact.getId());

        assertEquals(phone1.getCountryCode(), phonesFound.get(0).getCountryCode());
        assertEquals(phone1.getPhoneNumber(), phonesFound.get(0).getPhoneNumber());

        assertEquals(phone2.getCountryCode(), phonesFound.get(1).getCountryCode());
        assertEquals(phone2.getPhoneNumber(), phonesFound.get(1).getPhoneNumber());

        assertEquals(phone3.getCountryCode(), phonesFound.get(2).getCountryCode());
        assertEquals(phone3.getPhoneNumber(), phonesFound.get(2).getPhoneNumber());
    }
}
