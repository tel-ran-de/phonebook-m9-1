package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.dto.ContactDto;
import com.telran.phonebookapi.model.Contact;
import com.telran.phonebookapi.service.ContactService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/contact")
public class ContactController {

    ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("")
    public void addContact(@Valid @RequestBody ContactDto contactDto, Authentication auth) {
        contactService.add(contactDto, getUserId(auth));
    }

    @GetMapping("/{id}")
    public ContactDto getById(@PathVariable int id) {
        return contactService.getById(id);
    }

    @GetMapping("/{id}/extended")
    public ContactDto getByIdFullDetails(@PathVariable int id) {


        return contactService.getByIdFullDetails(id);
    }

    @PutMapping("")
    public void editContact(@Valid @RequestBody ContactDto contactDto) {
        contactService.editAllFields(contactDto);
    }

    @DeleteMapping("/{id}")
    public void removeById(@PathVariable int id) {
        contactService.removeById(id);
    }

    @GetMapping("/all")
    public List<ContactDto> requestAllContactsByUserEmailAndSortedByFirstName(Authentication auth) {

        return contactService.getAllContactsByUserId(getUserId(auth)).stream()
                .map(contact -> ContactDto
                        .builder()
                        .id(contact.getId())
                        .lastName(contact.getLastName())
                        .firstName(contact.getFirstName())
                        .description(contact.getDescription())
                        .build())
                .sorted(Comparator.comparing(o -> o.firstName))
                .collect(Collectors.toList());
    }

    @PostMapping("/profile")
    public void addProfile(@Valid @RequestBody ContactDto contactDto, Authentication auth) {
        contactService.addProfile(contactDto, getUserId(auth));
    }

    @PutMapping("/profile")
    public void editProfile(@Valid @RequestBody ContactDto contactDto) {
        contactService.editProfile(contactDto);
    }

    @GetMapping("/get-profile")
    public ContactDto getProfile(Authentication auth) {
        Contact contact = contactService.getProfile(getUserId(auth));
        return ContactDto
                .builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .description(contact.getDescription())
                .build();
    }

    private String getUserId(Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return userDetails.getUsername();
    }

}
