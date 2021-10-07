package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.dto.AddContactDto;
import com.telran.phonebookapi.dto.ContactDto;
import com.telran.phonebookapi.exception.UserAlreadyExistsException;
import com.telran.phonebookapi.mapper.ContactMapper;
import com.telran.phonebookapi.model.Contact;
import com.telran.phonebookapi.service.ContactService;
import com.telran.phonebookapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Contact")
@SecurityRequirement(name = "JWT")
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

    @Operation(summary = "Add new contact")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ContactDto addContact(Authentication auth, @Valid @RequestBody AddContactDto addNewContact) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Contact contact = contactService.add(addNewContact.firstName, addNewContact.lastName, addNewContact.description, email);
        return ContactDto.builder().
                id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .description(contact.getDescription())
                .build();
    }

    @Operation(summary = "Update contact")
    @PutMapping("")
    public void editContact(Authentication auth, @Valid @RequestBody ContactDto editContact) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Contact contact = contactService.getById(editContact.id);
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        contactService.edit(editContact.id, editContact.firstName, editContact.lastName, editContact.description);
    }

    @Operation(summary = "Get contact by contact id")
    @GetMapping("/{id}")
    public ContactDto getContactById(Authentication auth, @Parameter(description = "contact id", example = "1") @PathVariable int id) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Contact contact = contactService.getById(id);
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        return ContactDto.builder().
                id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .description(contact.getDescription())
                .build();
    }

    @Operation(summary = "Get user profile", description = "Get the profile of the authorized user")
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

    @Operation(summary = "Delete contact by contact id")
    @DeleteMapping("/{id}")
    public void removeContactById(Authentication auth, @Parameter(description = "contact id", example = "1") @PathVariable int id) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Contact contact = contactService.getById(id);
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        contactService.removeById(id);
    }

    @Operation(summary = "Get list of contacts by authenticated user")
    @GetMapping("")
    public List<ContactDto> getAllContactsByAuthUser(Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        return contactService.getAllContactsByUserId(email).stream().map(contactMapper::mapContactToDto)
                .collect(Collectors.toList());
    }
}
