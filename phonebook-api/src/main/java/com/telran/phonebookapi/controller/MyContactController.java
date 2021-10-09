package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.dto.AddMyContactDto;
import com.telran.phonebookapi.dto.MyContactDto;
import com.telran.phonebookapi.mapper.MyContactMapper;
import com.telran.phonebookapi.model.MyContact;
import com.telran.phonebookapi.service.MyContactService;
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

@Tag(name = "My Contact")
@SecurityRequirement(name = "JWT")
@RestController
@RequestMapping("/api/my-contacts")
public class MyContactController {

    private final MyContactService myContactService;
    private final MyContactMapper contactMapper;

    public MyContactController(MyContactService myContactService, MyContactMapper contactMapper) {
        this.myContactService = myContactService;
        this.contactMapper = contactMapper;
    }

    @Operation(summary = "Add new contact")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public MyContactDto addContact(@RequestHeader(value = "Access-Token", required = false) String accessToken,
                                   Authentication auth, @Valid @RequestBody AddMyContactDto addNewContact) {
        String email = getUserEmail(auth);

        MyContact contact = myContactService.add(email, addNewContact.firstName, addNewContact.lastName,
                addNewContact.description, addNewContact.address, addNewContact.email, addNewContact.phone);

        return contactMapper.mapContactToDto(contact);
    }

    @Operation(summary = "Update contact")
    @PutMapping("")
    public MyContactDto editContact(@RequestHeader(value = "Access-Token", required = false) String accessToken,
                                    Authentication auth, @Valid @RequestBody MyContactDto editContact) {
        String email = getUserEmail(auth);

        MyContact contact = myContactService.edit(email, editContact.id,
                editContact.firstName, editContact.lastName,
                editContact.description, editContact.address,
                editContact.email, editContact.phone);

        return contactMapper.mapContactToDto(contact);
    }

    @Operation(summary = "Get contact by contact id")
    @GetMapping("/{id}")
    public MyContactDto getContactById(@RequestHeader(value = "Access-Token", required = false) String accessToken,
                                       Authentication auth, @Parameter(description = "contact id", example = "1") @PathVariable int id) {
        String email = getUserEmail(auth);

        MyContact contact = myContactService.getById(email, id);
        return contactMapper.mapContactToDto(contact);
    }

    @Operation(summary = "Delete contact by contact id")
    @DeleteMapping("/{id}")
    public void removeContactById(@RequestHeader(value = "Access-Token", required = false) String accessToken,
                                  Authentication auth, @Parameter(description = "contact id", example = "1") @PathVariable int id) {
        String email = getUserEmail(auth);

        myContactService.removeById(email, id);
    }


    @Operation(summary = "Delete all contact by authenticated user")
    @DeleteMapping("/all")
    public void removeAllContactsById(@RequestHeader(value = "Access-Token", required = false) String accessToken,
                                      Authentication auth) {
        String email = getUserEmail(auth);

        myContactService.removeAllContactsByUser(email);
    }

    @Operation(summary = "Get list of contacts by authenticated user")
    @GetMapping("")
    public List<MyContactDto> getAllContactsByAuthUser(@RequestHeader(value = "Access-Token", required = false) String accessToken,
                                                       Authentication auth) {
        String email = getUserEmail(auth);

        return myContactService.getAllContactsByUserId(email)
                .stream()
                .map(contactMapper::mapContactToDto)
                .collect(Collectors.toList());
    }

    private String getUserEmail(Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        return email.toLowerCase().trim();
    }
}
