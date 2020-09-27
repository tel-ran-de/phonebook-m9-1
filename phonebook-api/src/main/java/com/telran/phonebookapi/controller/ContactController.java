package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.dto.ContactDto;
import com.telran.phonebookapi.exception.UserAlreadyExistsException;
import com.telran.phonebookapi.mapper.ContactMapper;
import com.telran.phonebookapi.model.Contact;
import com.telran.phonebookapi.service.ContactService;
import com.telran.phonebookapi.service.UserService;
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
    UserService userService;
    ContactService contactService;
    ContactMapper contactMapper;

    public ContactController(UserService userService, ContactService contactService, ContactMapper contactMapper) {
        this.userService = userService;
        this.contactService = contactService;
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
        }
        return ContactDto.builder()
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .description(contact.getDescription())
                .build();
    }

    @PutMapping("")
    public void editContact(Authentication auth, @Valid @RequestBody ContactDto contactDto) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Contact contact = contactService.getById(contactDto.id);
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        contactService.edit(contactDto.id, contactDto.firstName, contactDto.lastName, contactDto.description);
    }


    @DeleteMapping("/{id}")
    public void removeById(Authentication auth, @PathVariable int id) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Contact contact = contactService.getById(id);
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        contactService.removeById(id);
    }


    @GetMapping("")
    public List<ContactDto> requestAllContactsByUserEmail(Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        return contactService.getAllContactsByUserId(email).stream().map(contactMapper::mapContactToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/profile")
    public ContactDto getProfile(Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Contact myProfile = contactService.getProfile(email);
        return ContactDto.builder()
                .id(myProfile.getId())
                .firstName(myProfile.getFirstName())
                .lastName(myProfile.getLastName())
                .description(myProfile.getDescription())
                .build();
    }

    @PutMapping("/profile")
    public void editProfile(Authentication auth, @Valid @RequestBody ContactDto myProfileDto) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        contactService.editProfile(email, myProfileDto.firstName, myProfileDto.lastName, myProfileDto.description);
    }

}
