package com.telran.phonebookapi.service;

import com.telran.phonebookapi.mapper.AddressMapper;
import com.telran.phonebookapi.mapper.ContactMapper;
import com.telran.phonebookapi.mapper.EmailMapper;
import com.telran.phonebookapi.mapper.PhoneMapper;
import com.telran.phonebookapi.model.Contact;
import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.persistance.*;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static com.telran.phonebookapi.service.UserService.USER_DOES_NOT_EXIST;

@Service
public class ContactService {

    static final String CONTACT_DOES_NOT_EXIST = "Error! This contact doesn't exist in our DB";

    IUserRepository userRepository;
    IContactRepository contactRepository;
    IAddressRepository addressRepository;
    IPhoneRepository phoneRepository;
    IEmailRepository emailRepository;
    ContactMapper contactMapper;
    AddressMapper addressMapper;
    PhoneMapper phoneMapper;
    EmailMapper emailMapper;

    public ContactService(IUserRepository userRepository, IContactRepository contactRepository, IAddressRepository addressRepository, IPhoneRepository phoneRepository, IEmailRepository emailRepository, ContactMapper contactMapper, AddressMapper addressMapper, PhoneMapper phoneMapper, EmailMapper emailMapper) {
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
        this.addressRepository = addressRepository;
        this.phoneRepository = phoneRepository;
        this.emailRepository = emailRepository;
        this.contactMapper = contactMapper;
        this.addressMapper = addressMapper;
        this.phoneMapper = phoneMapper;
        this.emailMapper = emailMapper;
    }

    public void add(String firstName, String lastName, String description, String email) {
        String userId = email.toLowerCase().trim();

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(USER_DOES_NOT_EXIST));
        Contact contact = new Contact(firstName, user);
        contact.setLastName(lastName);
        contact.setDescription(description);
        contactRepository.save(contact);
    }

    public Contact getById(int id) {

        return contactRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(CONTACT_DOES_NOT_EXIST));
    }

    public void edit(int id, String firstName, String lastName, String description) {
        Contact contact = contactRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(CONTACT_DOES_NOT_EXIST));
        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        contact.setDescription(description);
        contactRepository.save(contact);
    }

    public void removeById(int id) {
        contactRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(CONTACT_DOES_NOT_EXIST));
        contactRepository.deleteById(id);
    }

    public List<Contact> getAllContactsByUserId(String email) {
        String userId = email.toLowerCase().trim();

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(USER_DOES_NOT_EXIST));
        Contact profile = user.getMyProfile();
        List<Contact> contacts = user.getContacts();
        return contacts
                .stream()
                .filter(contact -> contact.getId() != profile.getId())
                .collect(Collectors.toList());
    }

    public Contact getProfile(String email) {
        String userId = email.toLowerCase().trim();

        return userRepository.findById(userId).get().getMyProfile();
    }

    public void editProfile(String email, String firstName, String lastName, String description) {
        String userId = email.toLowerCase().trim();
        Contact myProfile = userRepository.findById(userId).get().getMyProfile();

        myProfile.setFirstName(firstName);
        myProfile.setLastName(lastName);
        myProfile.setDescription(description);
        contactRepository.save(myProfile);
    }
}
