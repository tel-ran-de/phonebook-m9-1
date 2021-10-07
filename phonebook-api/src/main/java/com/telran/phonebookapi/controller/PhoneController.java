package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.dto.AddPhoneDto;
import com.telran.phonebookapi.dto.PhoneDto;
import com.telran.phonebookapi.exception.UserAlreadyExistsException;
import com.telran.phonebookapi.mapper.PhoneMapper;
import com.telran.phonebookapi.model.Contact;
import com.telran.phonebookapi.model.Phone;
import com.telran.phonebookapi.service.ContactService;
import com.telran.phonebookapi.service.PhoneService;
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

import static com.telran.phonebookapi.controller.ContactController.CONTACT_DOES_NOT_BELONG;

@RestController
@RequestMapping("/api/phone")
@Api(tags = "Phone API")
public class PhoneController {

    PhoneService phoneService;
    ContactService contactService;
    PhoneMapper phoneMapper;

    public PhoneController(PhoneService phoneService, ContactService contactService, PhoneMapper phoneMapper) {
        this.phoneService = phoneService;
        this.contactService = contactService;
        this.phoneMapper = phoneMapper;
    }

    @ApiOperation(value = "add new phone", authorizations = {@Authorization(value = "JWT")}, tags = {"add"})
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

    @ApiOperation(value = "update phone", authorizations = {@Authorization(value = "JWT")}, tags = {"update"})
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

    @ApiOperation(value = "get phone by phone id", authorizations = {@Authorization(value = "JWT")}, tags = {"get by id"})
    @GetMapping("/{id}")
    public PhoneDto getPhoneById(Authentication auth, @ApiParam(value = "phone id", example = "1") @PathVariable int id) {
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

    @ApiOperation(value = "delete phone by phone id", authorizations = {@Authorization(value = "JWT")}, tags = {"delete by id"})
    @DeleteMapping("/{id}")
    public void removePhoneById(Authentication auth, @ApiParam(value = "phone id", example = "1") @PathVariable int id) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Phone phone = phoneService.getById(id);
        Contact contact = phone.getContact();
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        phoneService.removeById(id);
    }

    @ApiOperation(value = "get all phones by contact id", authorizations = {@Authorization(value = "JWT")}, tags = {"get all"})
    @GetMapping("/{contactId}/all")
    public List<PhoneDto> getAllPhonesByAuthUser(Authentication auth, @ApiParam(value = "contact id", example = "1") @PathVariable int contactId) {
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
