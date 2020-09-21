package com.telran.phonebookapi.controller;

import com.telran.phonebookapi.dto.AddressDto;
import com.telran.phonebookapi.exception.UserAlreadyExistsException;
import com.telran.phonebookapi.mapper.AddressMapper;
import com.telran.phonebookapi.model.Address;
import com.telran.phonebookapi.model.Contact;
import com.telran.phonebookapi.service.AddressService;
import com.telran.phonebookapi.service.ContactService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.telran.phonebookapi.controller.ContactController.CONTACT_DOES_NOT_BELONG;

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

    @PostMapping("")
    public void addAddress(Authentication auth, @RequestBody @Valid AddressDto addressDto) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Contact contact = contactService.getById(addressDto.contactId);
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        addressService.add(addressDto.street, addressDto.zip, addressDto.city, addressDto.country, contact.getId());
    }

    @PutMapping("")
    public void edit(Authentication auth, @RequestBody @Valid AddressDto addressDto) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Address address = addressService.getById(addressDto.id);
        Contact contact = contactService.getById(address.getContact().getId());
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        addressService.edit(address, addressDto.street, addressDto.zip, addressDto.city, addressDto.country);
    }

    @GetMapping("/{id}")
    public AddressDto getById(Authentication auth, @PathVariable int id) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Address address = addressService.getById(id);
        Contact contact = contactService.getById(address.getContact().getId());
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        return AddressDto.builder()
                .street(address.getStreet())
                .zip(address.getZip())
                .city(address.getCity())
                .country(address.getCountry())
                .contactId(contact.getId())
                .build();
    }

    @DeleteMapping("/{id}")
    public void removeById(Authentication auth, @PathVariable int id) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Address address = addressService.getById(id);
        Contact contact = contactService.getById(address.getContact().getId());
        if (!contact.getUser().getEmail().equals(email)) {
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);
        }
        addressService.removeById(id);
    }

    @GetMapping("/{contactId}/all")
    public List<AddressDto> getAllAddresses(Authentication auth, @PathVariable int contactId) {
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
