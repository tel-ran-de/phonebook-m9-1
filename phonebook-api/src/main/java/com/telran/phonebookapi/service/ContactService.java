package com.telran.phonebookapi.service;

import com.telran.phonebookapi.dto.AddressDto;
import com.telran.phonebookapi.dto.ContactDto;
import com.telran.phonebookapi.dto.EmailDto;
import com.telran.phonebookapi.dto.PhoneDto;
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

    public void add(ContactDto contactDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(UserService.USER_DOES_NOT_EXIST));
        Contact contact = new Contact(contactDto.firstName, user);

        if (contactDto.description == null)
            contact.setDescription("About " + contactDto.firstName + " " + contactDto.lastName + ".");
        else
            contact.setDescription(contactDto.description);

        contact.setLastName(contactDto.lastName);
        contactRepository.save(contact);
    }

    public ContactDto getById(int id) {
        Contact contact = contactRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(CONTACT_DOES_NOT_EXIST));
        return contactMapper.mapContactToDto(contact);
    }

    public void editAllFields(ContactDto contactDto) {
        Contact contact = contactRepository.findById(contactDto.id).orElseThrow(() -> new EntityNotFoundException(CONTACT_DOES_NOT_EXIST));
        contact.setFirstName(contactDto.firstName);
        contact.setLastName(contactDto.lastName);
        contact.setDescription(contactDto.description);
        contactRepository.save(contact);
    }

    public void removeById(int id) {
        contactRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(CONTACT_DOES_NOT_EXIST));
        contactRepository.deleteById(id);
    }

    public List<Contact> getAllContactsByUserId(String userId) {
        List<Contact> res = contactRepository.findAllByUserEmail(userId);
        User user = userRepository.findByEmail(userId).orElseThrow(() -> new EntityNotFoundException(UserService.USER_DOES_NOT_EXIST));

        Contact profile = user.getMyProfile();
        res.remove(profile);
        return res;
    }

    public void addProfile(ContactDto contactDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new EntityNotFoundException(UserService.USER_DOES_NOT_EXIST));

        Contact profile = user.getMyProfile();

        profile.setFirstName(contactDto.firstName);
        profile.setLastName(contactDto.lastName);
        profile.setDescription(contactDto.description);
        profile.setUser(user);

        user.addProfile(profile);

        contactRepository.save(profile);
    }

    public void editProfile(ContactDto contactDto) {
        Contact newProfile = contactRepository.findById(contactDto.id).orElseThrow(() -> new EntityNotFoundException(CONTACT_DOES_NOT_EXIST));
        newProfile.setFirstName(contactDto.firstName);
        newProfile.setLastName(contactDto.lastName);
        contactRepository.save(newProfile);
    }

    private List<PhoneDto> getAllPhonesByContact(Contact contact) {
        return phoneRepository.findAllByContactId(contact.getId())
                .stream()
                .map(phoneMapper::mapPhoneToDto)
                .collect(Collectors.toList());
    }

    private List<AddressDto> getAllAddressesByContact(Contact contact) {
        return addressRepository.findAllByContactId(contact.getId())
                .stream()
                .map(addressMapper::mapAddressToDto)
                .collect(Collectors.toList());
    }

    public List<EmailDto> getAllEmailsByContact(Contact contact) {
        return emailRepository.findAllByContactId(contact.getId())
                .stream()
                .map(email -> emailMapper.mapEmailToDto(email))
                .collect(Collectors.toList());
    }

    public Contact getProfile(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(UserService.USER_DOES_NOT_EXIST));
        return user.getMyProfile();
    }
}
