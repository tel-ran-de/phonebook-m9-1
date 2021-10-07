package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.dto.AddAddressDto;
import com.telran.phonebookapi.dto.AddressDto;
import com.telran.phonebookapi.exception.UserAlreadyExistsException;
import com.telran.phonebookapi.mapper.AddressMapper;
import com.telran.phonebookapi.model.Address;
import com.telran.phonebookapi.model.Contact;
import com.telran.phonebookapi.service.AddressService;
import com.telran.phonebookapi.service.ContactService;
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

@Tag(name = "Address")
@SecurityRequirement(name = "JWT")
@RestController
@RequestMapping("/api/address")
public class AddressController {

    AddressService addressService;
    ContactService contactService;
    AddressMapper addressMapper;

    public AddressController(AddressService addressService, ContactService contactService, AddressMapper addressMapper) {
        this.addressService = addressService;
        this.contactService = contactService;
        this.addressMapper = addressMapper;
    }

    @Operation(summary = "Add new address")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void addAddress(Authentication auth, @RequestBody @Valid AddAddressDto addAddress) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Contact contact = contactService.getById(addAddress.contactId);
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        addressService.add(addAddress.city, addAddress.country, addAddress.street, addAddress.zip, contact.getId());
    }

    @Operation(summary = "Update address")
    @PutMapping("")
    public void editAddress(Authentication auth, @RequestBody @Valid AddressDto editAddress) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Address address = addressService.getById(editAddress.id);
        Contact contact = address.getContact();
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        addressService.edit(address, editAddress.city, editAddress.country, editAddress.street, editAddress.zip);
    }

    @Operation(summary = "Get address by address id")
    @GetMapping("/{id}")
    public AddressDto getAddressById(Authentication auth, @Parameter(description = "address id", example = "1") @PathVariable int id) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Address address = addressService.getById(id);
        Contact contact = address.getContact();
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        return AddressDto.builder()
                .city(address.getCity())
                .country(address.getCountry())
                .street(address.getStreet())
                .zip(address.getZip())
                .contactId(contact.getId())
                .build();
    }

    @Operation(summary = "Delete address by address id")
    @DeleteMapping("/{id}")
    public void removeAddressById(Authentication auth, @Parameter(description = "address id", example = "1") @PathVariable int id) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Address address = addressService.getById(id);
        Contact contact = address.getContact();
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        addressService.removeById(id);
    }

    @Operation(summary = "Get list of addresses by contact id")
    @GetMapping("/{contactId}/all")
    public List<AddressDto> getAllAddressesByAuthUser(Authentication auth, @Parameter(description = "contact id", example = "1") @PathVariable int contactId) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Contact contact = contactService.getById(contactId);
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        return addressService.getAllAddressesByContactId(contactId).stream()
                .map(addressMapper::mapAddressToDto).collect(Collectors.toList());
    }
}
