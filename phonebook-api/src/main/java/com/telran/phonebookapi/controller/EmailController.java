package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.dto.AddEmailDto;
import com.telran.phonebookapi.dto.EmailDto;
import com.telran.phonebookapi.exception.UserAlreadyExistsException;
import com.telran.phonebookapi.mapper.EmailMapper;
import com.telran.phonebookapi.model.Contact;
import com.telran.phonebookapi.model.Email;
import com.telran.phonebookapi.service.ContactService;
import com.telran.phonebookapi.service.EmailService;
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
@RequestMapping("/api/email")
@Api(tags = "Email API")
public class EmailController {

    EmailService emailService;
    ContactService contactService;
    EmailMapper emailMapper;

    public EmailController(EmailService emailService, ContactService contactService, EmailMapper emailMapper) {
        this.emailService = emailService;
        this.contactService = contactService;
        this.emailMapper = emailMapper;
    }

    @ApiOperation(value = "add new email", authorizations = {@Authorization(value = "JWT")}, tags = {"add"})
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void addEmail(Authentication auth, @RequestBody @Valid AddEmailDto emailDto) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Contact contact = contactService.getById(emailDto.contactId);
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        emailService.add(emailDto.email, contact.getId());
    }

    @ApiOperation(value = "update email", authorizations = {@Authorization(value = "JWT")}, tags = {"update"})
    @PutMapping("")
    public void editEmail(Authentication auth, @RequestBody @Valid EmailDto emailDto) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String mail = userDetails.getUsername();
        Email email = emailService.getById(emailDto.id);
        Contact contact = email.getContact();
        if (!contact.getUser().getEmail().equals(mail)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        emailService.edit(email, emailDto.email);
    }

    @ApiOperation(value = "get email by email id", authorizations = {@Authorization(value = "JWT")}, tags = {"get by id"})
    @GetMapping("/{id}")
    public EmailDto getEmailById(Authentication auth, @ApiParam(value = "email id", example = "1") @PathVariable int id) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String mail = userDetails.getUsername();
        Email email = emailService.getById(id);
        Contact contact = email.getContact();
        if (!contact.getUser().getEmail().equals(mail)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        return EmailDto.builder()
                .email(email.getEmail())
                .build();
    }

    @ApiOperation(value = "delete email by email id", authorizations = {@Authorization(value = "JWT")})
    @DeleteMapping("/{id}")
    public void removeEmailById(Authentication auth, @ApiParam(value = "email id", example = "1") @PathVariable int id) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String mail = userDetails.getUsername();
        Email email = emailService.getById(id);
        Contact contact = email.getContact();
        if (!contact.getUser().getEmail().equals(mail)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        emailService.removeById(id);
    }

    @ApiOperation(value = "get all emails by contact id", authorizations = {@Authorization(value = "JWT")}, tags = {"get all"})
    @GetMapping("/{contactId}/all")
    public List<EmailDto> getAllEmailsByAuthUser(Authentication auth, @ApiParam(value = "contact id", example = "1") @PathVariable int contactId) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String mail = userDetails.getUsername();
        Contact contact = contactService.getById(contactId);
        if (!contact.getUser().getEmail().equals(mail)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        return emailService.getAllEmailsByContactId(contactId).stream()
                .map(emailMapper::mapEmailToDto).collect(Collectors.toList());
    }
}
