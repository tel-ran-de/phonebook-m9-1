package com.telran.phonebookapi.service;

import com.telran.phonebookapi.model.Contact;
import com.telran.phonebookapi.model.Email;
import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.persistance.IContactRepository;
import com.telran.phonebookapi.persistance.IEmailRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    IContactRepository contactRepository;

    @Mock
    IEmailRepository emailRepository;

    @InjectMocks
    EmailService emailService;

    @Test
    public void testAdd_contactExists_contactWithEmail() {
        User user = new User("test@gmail.com", "test");
        Contact contact = new Contact("Name", user);

        when(contactRepository.findById(contact.getId())).thenReturn(Optional.of(contact));

        Email email = new Email("mail@gmail.com", contact);
        emailService.add(email.getEmail(), email.getContact().getId());

        verify(emailRepository, times(1)).save(any());
        verify(emailRepository, times(1)).save(argThat(email1 -> email1.getEmail().equals(email.getEmail()) &&
                email1.getContact().getId() == email.getContact().getId())
        );
    }

    @Test
    public void testAdd_contactDoesNotExist_EntityNotFoundException() {
        User user = new User("test@gmail.com", "test");
        Contact contact = new Contact("Name", user);
        Email email = new Email("mail@gmail.com", contact);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> emailService.add(email.getEmail(), email.getContact().getId()+1));

        verify(contactRepository, times(1)).findById(any());
        assertEquals("Error! This contact doesn't exist in our DB", exception.getMessage());
    }

    @Test
    public void testEdit_emailExist_AllFieldsChanged() {

        User user = new User("test@gmail.com", "test");

        Contact oldContact = new Contact("TestName", user);
        Email oldEmail = new Email(oldContact);

        String email = "mail@gmail.com";

        emailService.edit(oldEmail, email);

        verify(emailRepository, times(1)).save(any());
        verify(emailRepository, times(1)).save(argThat(mail ->
                mail.getEmail().equals(email)
                        && mail.getContact().getId() == oldEmail.getContact().getId())
        );
    }

    @Captor
    ArgumentCaptor<Email> emailArgumentCaptor;

    @Test
    public void testRemoveById_emailExists_EmailDeleted() {

        User user = new User("test@gmail.com", "test");

        Contact contact = new Contact("TestName", user);
        Email email = new Email();
        contact.addEmail(email);

        emailService.removeById(email.getId());

        List<Email> capturedAddresses = emailArgumentCaptor.getAllValues();
        verify(emailRepository, times(1)).deleteById(email.getId());
        assertEquals(0, capturedAddresses.size());
    }

    @Test
    public void testGetById_contactWithAddress_Address() {

        User user = new User("test@gmail.com", "test");
        Contact contact = new Contact("TestName", user);
        Email email = new Email("mail@gmail.com", contact);

        when(emailRepository.findById(email.getId())).thenReturn(Optional.of(email));
        Email email1 = emailService.getById(email.getId());

        assertEquals(email1.getEmail(), email.getEmail());

        verify(emailRepository, times(1)).findById(argThat(
                id -> id == email.getId()));
    }

    @Test
    public void testGetById_wrongEmailId_EmailNotExist() {

        Email email = new Email();

        Exception exception = assertThrows(EntityNotFoundException.class, () -> emailService.getById(email.getId()));

        verify(emailRepository, times(1)).findById(any());
        assertEquals("Error! This email doesn't exist in our DB", exception.getMessage());
    }

    @Test
    public void testGetAllByContactId_ContactWithEmails_3Emails() {
        User user = new User("test@gmail.com", "test");
        Contact contact = new Contact("Name1", user);

        Email email1 = new Email("mail1@gmail.com", contact);
        Email email2 = new Email("mail2@gmail.com", contact);
        Email email3 = new Email("mail3@gmail.com", contact);

        List<Email> emails = new ArrayList<>();
        emails.add(email1);
        emails.add(email2);
        emails.add(email3);

        when(emailRepository.findAllByContactId(contact.getId())).thenReturn(emails);
        List<Email> emailsFound = emailService.getAllEmailsByContactId(contact.getId());

        assertEquals(email1.getEmail(), emailsFound.get(0).getEmail());
        assertEquals(email2.getEmail(), emailsFound.get(1).getEmail());
        assertEquals(email3.getEmail(), emailsFound.get(2).getEmail());
    }
}
