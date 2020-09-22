package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.dto.PhoneDto;
import com.telran.phonebookapi.exception.UserAlreadyExistsException;
import com.telran.phonebookapi.mapper.PhoneMapper;
import com.telran.phonebookapi.model.Contact;
import com.telran.phonebookapi.model.Phone;
import com.telran.phonebookapi.service.ContactService;
import com.telran.phonebookapi.service.PhoneService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.telran.phonebookapi.controller.ContactController.CONTACT_DOES_NOT_BELONG;

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

    @PostMapping("")
    public void addPhone(Authentication auth, @RequestBody @Valid PhoneDto phoneDto) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Contact contact = contactService.getById(phoneDto.contactId);
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        phoneService.add(phoneDto.countryCode, phoneDto.phoneNumber, contact.getId());
    }


    @PutMapping("")
    public void edit(Authentication auth, @RequestBody @Valid PhoneDto phoneDto) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Phone phone = phoneService.getById(phoneDto.id);
        Contact contact = phone.getContact();
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        phoneService.edit(phone, phoneDto.countryCode, phoneDto.phoneNumber);
    }


    @GetMapping("/{id}")
    public PhoneDto getById(Authentication auth, @PathVariable int id) {
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

    @DeleteMapping("/{id}")
    public void removeById(Authentication auth, @PathVariable int id) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Phone phone = phoneService.getById(id);
        Contact contact = phone.getContact();
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        phoneService.removeById(id);
    }


    @GetMapping("/{contactId}/all")
    public List<PhoneDto> getAllPhoneNumbers(Authentication auth, @PathVariable int contactId) {
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
