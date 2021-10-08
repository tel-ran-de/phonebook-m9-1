package com.telran.phonebookapi.service;

import com.telran.phonebookapi.exception.UserAlreadyExistsException;
import com.telran.phonebookapi.model.MyContact;
import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.persistance.IMyContactRepository;
import com.telran.phonebookapi.persistance.IUserRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static com.telran.phonebookapi.service.ContactService.CONTACT_DOES_NOT_EXIST;
import static com.telran.phonebookapi.service.UserService.USER_DOES_NOT_EXIST;

@Service
public class MyContactService {

    static final String CONTACT_DOES_NOT_BELONG = "Error! This contact doesn't belong this user";

    private final IUserRepository userRepository;
    private final IMyContactRepository contactRepository;

    public MyContactService(IUserRepository userRepository, IMyContactRepository contactRepository) {
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
    }


    public MyContact add(String userEmail, String firstName, String lastName, String description,
                         String address, String email, String phone) {
        User user = getUserById(userEmail);

        MyContact contact = new MyContact(firstName, user);

        contact.setLastName(lastName);
        contact.setDescription(description);

        contact.setAddress(address);
        contact.setEmail(email);
        contact.setPhone(phone);

        contactRepository.save(contact);
        return contact;
    }

    public MyContact getById(String userEmail, int id) {
        MyContact contact = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CONTACT_DOES_NOT_EXIST));

        if (!contact.getUser().getEmail().equalsIgnoreCase(userEmail))
            throw new UserAlreadyExistsException(CONTACT_DOES_NOT_BELONG);

        return contact;
    }

    public MyContact edit(String userEmail, int id,
                          String firstName, String lastName, String description,
                          String address, String email, String phone) {

        MyContact contact = getById(userEmail, id);

        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        contact.setDescription(description);

        contact.setAddress(address);
        contact.setEmail(email);
        contact.setPhone(phone);

        return contactRepository.save(contact);
    }

    public void removeById(String userEmail, int id) {
        getById(userEmail, id);
        contactRepository.deleteById(id);
    }

    public void removeAllContactsByUser(String userEmail) {
        User user = getUserById(userEmail);

        contactRepository.deleteAllByUser(user);
    }

    public List<MyContact> getAllContactsByUserId(String userEmail) {
        User user = getUserById(userEmail);
        return contactRepository.findAllByUser(user);
    }

    private User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_DOES_NOT_EXIST));
    }
}
