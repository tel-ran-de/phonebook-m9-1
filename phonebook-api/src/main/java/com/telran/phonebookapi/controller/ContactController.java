package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.dto.ContactDto;
import com.telran.phonebookapi.exception.UserAlreadyExistsException;
import com.telran.phonebookapi.mapper.AddressMapper;
import com.telran.phonebookapi.mapper.ContactMapper;
import com.telran.phonebookapi.mapper.EmailMapper;
import com.telran.phonebookapi.mapper.PhoneMapper;
import com.telran.phonebookapi.model.Contact;
import com.telran.phonebookapi.service.ContactService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api/contact")
public class ContactController {

    static final String CONTACT_DOES_NOT_BELONG = "Error! This contact doesn't belong this user";

    ContactService contactService;
    AddressMapper addressMapper;
    PhoneMapper phoneMapper;
    EmailMapper emailMapper;
    ContactMapper contactMapper;

    public ContactController(ContactService contactService, AddressMapper addressMapper, PhoneMapper phoneMapper, EmailMapper emailMapper, ContactMapper contactMapper) {
        this.contactService = contactService;
        this.addressMapper = addressMapper;
        this.phoneMapper = phoneMapper;
        this.emailMapper = emailMapper;
        this.contactMapper = contactMapper;
    }

    @PostMapping("")
    public void addContact(Authentication auth, @Valid @RequestBody ContactDto contactDto) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        contactService.add(contactDto.firstName, contactDto.lastName, contactDto.description, email);
    }

    @GetMapping("/{id}")
    public ContactDto getAllContactsByUserId(Authentication auth, @PathVariable int id) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Contact contact = contactService.getById(id);
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        } else {
            return ContactDto.builder()
                    .firstName(contact.getFirstName())
                    .lastName(contact.getLastName())
                    .description(contact.getDescription())
                    .build();
        }
    }

   /* @PutMapping("")
    public void editContact(Authentication auth, @Valid @RequestBody ContactDto contactDto) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Contact contact = contactService.getById(contactDto.id);
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        } else {
        contactService.edit(contactDto.id, contactDto.firstName, contactDto.lastName, contactDto.description);
    }

    @DeleteMapping("/{id}")
    public void removeById(@PathVariable int id) {
        contactService.removeById(id);
    }*/

    @GetMapping("")
    public List<ContactDto> requestAllContactsByUserEmail(Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        return contactService.getAllContactsByUserId(email).stream().map(contactMapper::mapContactToDto)
                .collect(Collectors.toList());
    }

   /*@PostMapping("/profile")
    public void addProfile(@Valid @RequestBody ContactDto contactDto) {
        contactService.addProfile(contactDto);
    }

    @PutMapping("/profile")
    public void editProfile(@Valid @RequestBody ContactDto contactDto) {
        contactService.editProfile(contactDto);
    }

    @PostMapping("/get-profile")
    public ContactDto getProfile(@Valid @RequestBody UserEmailDto userEmailDto) {
        return contactService.getProfile(userEmailDto);
    }*/

}
