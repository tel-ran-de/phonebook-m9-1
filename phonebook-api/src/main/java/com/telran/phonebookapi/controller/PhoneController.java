package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.dto.AddPhoneDto;
import com.telran.phonebookapi.dto.PhoneDto;
import com.telran.phonebookapi.exception.UserAlreadyExistsException;
import com.telran.phonebookapi.mapper.PhoneMapper;
import com.telran.phonebookapi.model.Contact;
import com.telran.phonebookapi.model.Phone;
import com.telran.phonebookapi.service.ContactService;
import com.telran.phonebookapi.service.PhoneService;
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

import static com.telran.phonebookapi.controller.ContactController.CONTACT_DOES_NOT_BELONG;

@Tag(name = "Phone")
@SecurityRequirement(name = "JWT")
@RestController
@RequestMapping("/api/phone")
public class PhoneController {

    PhoneService phoneService;
    ContactService contactService;
    PhoneMapper phoneMapper;

    public PhoneController(PhoneService phoneService, ContactService contactService, PhoneMapper phoneMapper) {
        this.phoneService = phoneService;
        this.contactService = contactService;
        this.phoneMapper = phoneMapper;
    }

    @Operation(summary = "Add new phone")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void addPhone(Authentication auth, @RequestBody @Valid AddPhoneDto phoneDto) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Contact contact = contactService.getById(phoneDto.contactId);
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        phoneService.add(phoneDto.countryCode, phoneDto.phoneNumber, contact.getId());
    }

    @Operation(summary = "Update phone")
    @PutMapping("")
    public void editPhone(Authentication auth, @RequestBody @Valid PhoneDto phoneDto) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Phone phone = phoneService.getById(phoneDto.id);
        Contact contact = phone.getContact();
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        phoneService.edit(phone, phoneDto.countryCode, phoneDto.phoneNumber);
    }

    @Operation(summary = "Get phone by phone id")
    @GetMapping("/{id}")
    public PhoneDto getPhoneById(Authentication auth, @Parameter(description = "phone id", example = "1") @PathVariable int id) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Phone phone = phoneService.getById(id);
        Contact contact = phone.getContact();
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        return PhoneDto.builder()
                .countryCode(phone.getCountryCode())
                .phoneNumber(phone.getPhoneNumber())
                .contactId(contact.getId())
                .build();
    }

    @Operation(summary = "Delete phone by phone id")
    @DeleteMapping("/{id}")
    public void removePhoneById(Authentication auth, @Parameter(description = "phone id", example = "1") @PathVariable int id) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Phone phone = phoneService.getById(id);
        Contact contact = phone.getContact();
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        phoneService.removeById(id);
    }

    @Operation(summary = "Get list of phones by contact id")
    @GetMapping("/{contactId}/all")
    public List<PhoneDto> getAllPhonesByAuthUser(Authentication auth, @Parameter(description = "contact id", example = "1") @PathVariable int contactId) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Contact contact = contactService.getById(contactId);
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        return phoneService.getAllPhoneNumbersByContactId(contactId).stream()
                .map(phoneMapper::mapPhoneToDto).collect(Collectors.toList());
    }
}
