package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.dto.ContactDto;
import com.telran.phonebookapi.dto.UserEmailDto;
import com.telran.phonebookapi.mapper.AddressMapper;
import com.telran.phonebookapi.mapper.ContactMapper;
import com.telran.phonebookapi.mapper.EmailMapper;
import com.telran.phonebookapi.mapper.PhoneMapper;
import com.telran.phonebookapi.model.Contact;
import com.telran.phonebookapi.service.ContactService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/contact")
public class ContactController {

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
    @PreAuthorize("isAuthenticated()")
    public void addContact(Authentication auth, @Valid @RequestBody ContactDto contactDto) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        contactService.add(contactDto.firstName, email);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ContactDto getById(@PathVariable int id) {
        Contact contact = contactService.getById(id);
        return ContactDto.builder().
                firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .description(contact.getDescription())
                .userId(contact.getUser().getEmail())
                .addresses(contact.getAddresses().stream().map(addressMapper::mapAddressToDto).collect(Collectors.toList()))
                .phoneNumbers(contact.getPhoneNumbers().stream().map(phoneMapper::mapPhoneToDto).collect(Collectors.toList()))
                .emails(contact.getEmails().stream().map(emailMapper::mapEmailToDto).collect(Collectors.toList()))
                .build();
    }

    @PutMapping("")
    @PreAuthorize("isAuthenticated()")
    public void editContact(@Valid @RequestBody ContactDto contactDto) {
        contactService.edit(contactDto.id, contactDto.firstName, contactDto.lastName, contactDto.description);
    }

    @DeleteMapping("/{id}")
    public void removeById(@PathVariable int id) {
        contactService.removeById(id);
    }

    @PostMapping("/all")
    public List<ContactDto> requestAllContactsByUserEmail(@Valid @RequestBody UserEmailDto userEmailDto) {
        return contactService.getAllContactsByUserId(userEmailDto.email).stream().map(contactMapper::mapContactToDto)
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
