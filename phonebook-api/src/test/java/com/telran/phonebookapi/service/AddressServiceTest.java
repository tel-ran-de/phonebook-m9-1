package com.telran.phonebookapi.service;

import com.telran.phonebookapi.model.Address;
import com.telran.phonebookapi.model.Contact;

import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.persistance.IAddressRepository;
import com.telran.phonebookapi.persistance.IContactRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    IContactRepository contactRepository;

    @Mock
    IAddressRepository addressRepository;

    @InjectMocks
    AddressService addressService;

    @Test
    public void testAdd_contactExists_contactWithAddress() {
        User user = new User("test@gmail.com", "test");
        Contact contact = new Contact("Name", user);

        when(contactRepository.findById(contact.getId())).thenReturn(Optional.of(contact));

        Address address = new Address("Berlin", "Germany", "Strasse", "10000", contact);
        addressService.add(address.getCity(), address.getCountry(), address.getStreet(), address.getZip(),
                address.getContact().getId());

        verify(addressRepository, times(1)).save(any());
        verify(addressRepository, times(1)).save(argThat(address1 ->
                address1.getCity().equals(address.getCity())
               && address1.getContact().getId() == address.getContact().getId())
        );
    }

    @Test
    public void testAdd_contactDoesNotExist_EntityNotFoundException() {
        Contact contact = new Contact();
        Address address = new Address("Street", "Zip", "City", "Country", contact);

        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                addressService.add(address.getCity(), address.getCountry(), address.getStreet(), address.getZip(),
                        address.getContact().getId()+1));

        verify(contactRepository, times(1)).findById(any());
        assertEquals("Error! This contact doesn't exist in our DB", exception.getMessage());
    }

    @Test
    public void testEdit_addressExist_AllFieldsChanged() {

        User user = new User("test@gmail.com", "test");

        Contact oldContact = new Contact("TestName", user);
        Address oldAddress = new Address(oldContact);

        String city = "city";
        String country = "country";
        String street = "street";
        String zip = "zip";

        addressService.edit(oldAddress, city, country, street, zip);

        verify(addressRepository, times(1)).save(any());
        verify(addressRepository, times(1)).save(argThat(address ->
                address.getCity().equals(city)
                        && address.getCountry().equals(country)
                        && address.getStreet().equals(street)
                        && address.getZip().equals(zip)
                        && address.getContact().getId() == oldContact.getId())
        );
    }

    @Captor
    ArgumentCaptor<Address> addressArgumentCaptor;

    @Test
    public void testRemoveById_addressExists_AddressDeleted() {

        User user = new User("test@gmail.com", "test");

        Contact contact = new Contact("TestName", user);
        Address address = new Address();
        contact.addAddress(address);

        addressService.removeById(address.getId());

        List<Address> capturedAddresses = addressArgumentCaptor.getAllValues();
        verify(addressRepository, times(1)).deleteById(address.getId());
        assertEquals(0, capturedAddresses.size());
    }

    @Test
    public void testGetById_contactWithAddress_Address() {

        User user = new User("test@gmail.com", "test");
        Contact contact = new Contact("TestName", user);
        Address address = new Address("Berlin", "Germany", "Strasse", "10000", contact);

        when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));
        Address address1 = addressService.getById(address.getId());

        assertEquals(address1.getZip(), address.getZip());
        assertEquals(address1.getCountry(), address.getCountry());
        assertEquals(address1.getCity(), address.getCity());
        assertEquals(address1.getStreet(), address.getStreet());

       verify(addressRepository, times(1)).findById(argThat(
               id -> id == address.getId()));
    }

    @Test
    public void testGetById_wrongAddressId_AddressNotExist() {

        Address address = new Address();

        Exception exception = assertThrows(EntityNotFoundException.class, () -> addressService.getById(address.getId()+1));

        verify(addressRepository, times(1)).findById(any());
        assertEquals("Error! This address doesn't exist in our DB", exception.getMessage());
    }

    @Test
    public void testGetAllByContactId_ContactWithAdresses_3Addresses() {
        User user = new User("test@gmail.com", "test");
        Contact contact = new Contact("Name1", user);

        Address address1 = new Address("Berlin", "Germany", "Strasse1", "10000", contact);
        Address address2 = new Address("Berlin", "Germany", "Strasse2", "10000", contact);
        Address address3 = new Address("Berlin", "Germany", "Strasse3", "10000", contact);

        List<Address> adresses = new ArrayList<>();
        adresses.add(address1);
        adresses.add(address2);
        adresses.add(address3);

        when(addressRepository.findAllByContactId(contact.getId())).thenReturn(adresses);
        List<Address> addressesFound = addressService.getAllAddressesByContactId(contact.getId());

        assertEquals(address1.getZip(), addressesFound.get(0).getZip());
        assertEquals(address1.getCountry(), addressesFound.get(0).getCountry());
        assertEquals(address1.getCity(), addressesFound.get(0).getCity());
        assertEquals(address1.getStreet(), addressesFound.get(0).getStreet());

        assertEquals(address2.getZip(), addressesFound.get(1).getZip());
        assertEquals(address2.getCountry(), addressesFound.get(1).getCountry());
        assertEquals(address2.getCity(), addressesFound.get(1).getCity());
        assertEquals(address2.getStreet(), addressesFound.get(1).getStreet());

        assertEquals(address3.getZip(), addressesFound.get(2).getZip());
        assertEquals(address3.getCountry(), addressesFound.get(2).getCountry());
        assertEquals(address3.getCity(), addressesFound.get(2).getCity());
        assertEquals(address3.getStreet(), addressesFound.get(2).getStreet());
    }
}

