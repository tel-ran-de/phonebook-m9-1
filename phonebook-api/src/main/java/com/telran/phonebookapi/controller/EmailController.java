package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.dto.AddEmailDto;
import com.telran.phonebookapi.dto.EmailDto;
import com.telran.phonebookapi.exception.UserAlreadyExistsException;
import com.telran.phonebookapi.mapper.EmailMapper;
import com.telran.phonebookapi.model.Contact;
import com.telran.phonebookapi.model.Email;
import com.telran.phonebookapi.service.ContactService;
import com.telran.phonebookapi.service.EmailService;
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

@Tag(name = "Email")
@SecurityRequirement(name = "JWT")
@RestController
@RequestMapping("/api/email")
public class EmailController {

    EmailService emailService;
    ContactService contactService;
    EmailMapper emailMapper;

    public EmailController(EmailService emailService, ContactService contactService, EmailMapper emailMapper) {
        this.emailService = emailService;
        this.contactService = contactService;
        this.emailMapper = emailMapper;
    }

    @Operation(summary = "Add new email")
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

    @Operation(summary = "Update email")
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

    @Operation(summary = "Get email by email id")
    @GetMapping("/{id}")
    public EmailDto getEmailById(Authentication auth, @Parameter(description = "email id", example = "1") @PathVariable int id) {
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

    @Operation(summary = "Delete email by email id")
    @DeleteMapping("/{id}")
    public void removeEmailById(Authentication auth, @Parameter(description = "email id", example = "1") @PathVariable int id) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String mail = userDetails.getUsername();
        Email email = emailService.getById(id);
        Contact contact = email.getContact();
        if (!contact.getUser().getEmail().equals(mail)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        emailService.removeById(id);
    }

    @Operation(summary = "Get list of emails by contact id")
    @GetMapping("/{contactId}/all")
    public List<EmailDto> getAllEmailsByAuthUser(Authentication auth, @Parameter(description = "contact id", example = "1") @PathVariable int contactId) {
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
