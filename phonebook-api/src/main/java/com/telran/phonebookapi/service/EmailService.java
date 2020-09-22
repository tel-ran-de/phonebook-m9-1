package com.telran.phonebookapi.service;

import com.telran.phonebookapi.mapper.EmailMapper;
import com.telran.phonebookapi.model.Contact;
import com.telran.phonebookapi.model.Email;
import com.telran.phonebookapi.persistance.IContactRepository;
import com.telran.phonebookapi.persistance.IEmailRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {

    static final String EMAIL_DOES_NOT_EXIST = "Error! This email doesn't exist in our DB";

    IContactRepository contactRepository;
    IEmailRepository emailRepository;
    EmailMapper emailMapper;

    public EmailService(IContactRepository contactRepository, IEmailRepository iEmailRepository, EmailMapper emailMapper) {
        this.contactRepository = contactRepository;
        this.emailRepository = iEmailRepository;
        this.emailMapper = emailMapper;
    }

    public void add(String mail, int contactId) {
        Contact contact = contactRepository.findById(contactId).orElseThrow(() -> new EntityNotFoundException(ContactService.CONTACT_DOES_NOT_EXIST));
        Email email = new Email(mail, contact);
        emailRepository.save(email);
    }

    public void edit(Email email, String mail) {
        email.setEmail(mail);
        emailRepository.save(email);
    }

    public Email getById(int id) {
        return emailRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(EMAIL_DOES_NOT_EXIST));
    }

    public void removeById(int id) {
        emailRepository.deleteById(id);
    }

    public List<Email> getAllEmailsByContactId(int contactId) {
        return new ArrayList<>(emailRepository.findAllByContactId(contactId));
    }
}
