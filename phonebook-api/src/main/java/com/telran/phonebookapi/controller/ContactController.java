package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.dto.AddContactDto;
import com.telran.phonebookapi.dto.ContactDto;
import com.telran.phonebookapi.exception.UserAlreadyExistsException;
import com.telran.phonebookapi.mapper.ContactMapper;
import com.telran.phonebookapi.model.Contact;
import com.telran.phonebookapi.service.ContactService;
import com.telran.phonebookapi.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contact")
@Api(tags = "Contact API")
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

    @ApiOperation(value = "add new contact", authorizations = {@Authorization(value = "JWT")}, tags = {"add"})
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

    @ApiOperation(value = "update contact", authorizations = {@Authorization(value = "JWT")}, tags = {"update"})
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

    @ApiOperation(value = "get contact by contact id", authorizations = {@Authorization(value = "JWT")}, tags = {"get by id"})
    @GetMapping("/{id}")
    public ContactDto getContactById(Authentication auth, @ApiParam(value = "contact id", example = "1") @PathVariable int id) {
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

    @ApiOperation(value = "get user profile", authorizations = {@Authorization(value = "JWT")}
            , notes = "get the profile of the authorized user", tags = {"get by id"})
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

    @ApiOperation(value = "delete contact by contact id", authorizations = {@Authorization(value = "JWT")}, tags = {"delete by id"})
    @DeleteMapping("/{id}")
    public void removeContactById(Authentication auth, @ApiParam(value = "contact id", example = "1") @PathVariable int id) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Contact contact = contactService.getById(id);
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        contactService.removeById(id);
    }

    @ApiOperation(value = "get all contacts by authenticated user", authorizations = {@Authorization(value = "JWT")}, tags = {"get all"})
    @GetMapping("")
    public List<ContactDto> getAllContactsByAuthUser(Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        return contactService.getAllContactsByUserId(email).stream().map(contactMapper::mapContactToDto)
                .collect(Collectors.toList());
    }
}
