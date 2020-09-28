package com.telran.phonebookapi.service;

import com.telran.phonebookapi.mapper.AddressMapper;
import com.telran.phonebookapi.model.Address;
import com.telran.phonebookapi.model.Contact;
import com.telran.phonebookapi.persistance.IAddressRepository;
import com.telran.phonebookapi.persistance.IContactRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AddressService {

    static final String ADDRESS_DOES_NOT_EXIST = "Error! This address doesn't exist in our DB";

    IContactRepository contactRepository;
    IAddressRepository addressRepository;
    AddressMapper addressMapper;

    public AddressService(IContactRepository contactRepository, IAddressRepository addressRepository, AddressMapper addressMapper) {
        this.contactRepository = contactRepository;
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    public void add(String city, String country, String street, String zip, int contactId) {
        Contact contact = contactRepository.findById(contactId).orElseThrow(() -> new EntityNotFoundException(ContactService.CONTACT_DOES_NOT_EXIST));
        Address address = new Address(city, country, street, zip, contact);
        addressRepository.save(address);
    }

    public void edit(Address address, String city, String country, String street, String zip) {
        address.setCity(city);
        address.setCountry(country);
        address.setStreet(street);
        address.setZip(zip);
        addressRepository.save(address);
    }

    public Address getById(int id) {
        return addressRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ADDRESS_DOES_NOT_EXIST));
    }

    public void removeById(int id) {
        addressRepository.deleteById(id);
    }

    public List<Address> getAllAddressesByContactId(int contactId) {
        return new ArrayList<>(addressRepository.findAllByContactId(contactId));
    }
}
