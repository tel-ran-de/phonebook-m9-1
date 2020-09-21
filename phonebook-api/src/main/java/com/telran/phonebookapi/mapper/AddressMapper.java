package com.telran.phonebookapi.mapper;

import com.telran.phonebookapi.dto.AddressDto;
import com.telran.phonebookapi.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressDto mapAddressToDto(Address address) {
        return new AddressDto(address.getId(), address.getStreet(), address.getZip(), address.getCity(), address.getCountry(), address.getContact().getId());
    }
}
