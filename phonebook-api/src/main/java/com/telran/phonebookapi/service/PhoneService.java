package com.telran.phonebookapi.service;

import com.telran.phonebookapi.mapper.PhoneMapper;
import com.telran.phonebookapi.model.Contact;
import com.telran.phonebookapi.model.Phone;
import com.telran.phonebookapi.persistance.IContactRepository;
import com.telran.phonebookapi.persistance.IPhoneRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PhoneService {

    static final String PHONE_DOES_NOT_EXIST = "Error! This phone number doesn't exist in our DB";

    IContactRepository contactRepository;
    IPhoneRepository phoneRepository;
    PhoneMapper phoneMapper;

    public PhoneService(IContactRepository contactRepository, IPhoneRepository phoneRepository, PhoneMapper phoneMapper) {
        this.contactRepository = contactRepository;
        this.phoneRepository = phoneRepository;
        this.phoneMapper = phoneMapper;
    }

    public void add(String countryCode, String phoneNumber, int contactId) {
        Contact contact = contactRepository.findById(contactId).orElseThrow(() -> new EntityNotFoundException(ContactService.CONTACT_DOES_NOT_EXIST));
        Phone phone = new Phone(countryCode, phoneNumber, contact);
        phoneRepository.save(phone);
    }

    public void edit(Phone phone, String countryCode, String phoneNumber) {
        phone.setCountryCode(countryCode);
        phone.setPhoneNumber(phoneNumber);
        phoneRepository.save(phone);
    }

    public Phone getById(int id) {
       return phoneRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(PHONE_DOES_NOT_EXIST));
    }

    public void removeById(int id) {
        phoneRepository.deleteById(id);
    }

    public List<Phone> getAllPhoneNumbersByContactId(int contactId) {
        return new ArrayList<>(phoneRepository.findAllByContactId(contactId));
    }
}
