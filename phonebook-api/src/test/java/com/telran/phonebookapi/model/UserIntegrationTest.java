package com.telran.phonebookapi.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest()
class UserIntegrationTest {

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void testAddContact_userWithoutContacts_oneContact() {
        User user = new User("email7", "password");
        Contact contact = new Contact("ContactName8", user);

        user.addContact(contact);

        entityManager.persist(user);
        entityManager.flush();
        entityManager.clear();

        User userFromDb = entityManager.find(User.class, "email7");

        assertEquals(1, userFromDb.getContacts().size());

        Contact contactFromDB = user.getContacts().get(0);
        assertEquals(contactFromDB.getName(), "ContactName8");
    }
}
