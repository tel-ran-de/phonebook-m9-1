package com.telran.phonebookapi.persistance;

import com.telran.phonebookapi.model.MyContact;
import com.telran.phonebookapi.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class IMyContactRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    IMyContactRepository contactRepository;

    @Test
    public void testFindByAllByUser_oneRecord_found() {

        User ivan = new User("ivan@gmail.com", "12345");
        MyContact ivanContact1 = new MyContact("Mama Ivan", ivan);
        MyContact ivanContact2 = new MyContact("Papa Ivan", ivan);

        entityManager.persist(ivan);
        entityManager.persist(ivanContact1);
        entityManager.persist(ivanContact2);

        entityManager.flush();
        entityManager.clear();
        List<MyContact> foundContacts = contactRepository.findAllByUser(ivan);
        assertEquals(2, foundContacts.size());
        assertEquals("Mama Ivan", foundContacts.get(0).getFirstName());
        assertEquals("Papa Ivan", foundContacts.get(1).getFirstName());
    }

    @Test
    public void testFindByAllByUser_TwoUsers_found() {

        User ivan = new User("ivan@gmail.com", "12345");
        MyContact ivanContact1 = new MyContact("Mama Ivan", ivan);
        MyContact ivanContact2 = new MyContact("Papa Ivan", ivan);
        MyContact ivanContact3 = new MyContact("Maria", ivan);

        User max = new User("vasja@gmail.com", "12345");
        MyContact maxContact1 = new MyContact("Max contact 1", max);
        MyContact maxContact2 = new MyContact("Max contact 2", max);

        entityManager.persist(ivan);
        entityManager.persist(ivanContact1);
        entityManager.persist(ivanContact2);
        entityManager.persist(ivanContact3);
        entityManager.persist(max);
        entityManager.persist(maxContact1);
        entityManager.persist(maxContact2);

        entityManager.flush();
        entityManager.clear();
        List<MyContact> foundContacts = contactRepository.findAllByUser(ivan);
        assertEquals(3, foundContacts.size());
        assertEquals("Mama Ivan", foundContacts.get(0).getFirstName());
        assertEquals("Papa Ivan", foundContacts.get(1).getFirstName());
        assertEquals("Maria", foundContacts.get(2).getFirstName());
    }

    @Test
    public void testDeleteAllByUser_TwoUsers_found() {

        User ivan = new User("ivan@gmail.com", "12345");
        MyContact ivanContact1 = new MyContact("Mama Ivan", ivan);
        MyContact ivanContact2 = new MyContact("Papa Ivan", ivan);
        MyContact ivanContact3 = new MyContact("Maria", ivan);

        User max = new User("vasja@gmail.com", "12345");
        MyContact maxContact1 = new MyContact("Max contact 1", max);
        MyContact maxContact2 = new MyContact("Max contact 2", max);

        entityManager.persist(ivan);
        entityManager.persist(ivanContact1);
        entityManager.persist(ivanContact2);
        entityManager.persist(ivanContact3);
        entityManager.persist(max);
        entityManager.persist(maxContact1);
        entityManager.persist(maxContact2);

        entityManager.flush();
        entityManager.clear();
        contactRepository.deleteAllByUser(ivan);

        List<MyContact> foundContactsByIvan = contactRepository.findAllByUser(ivan);
        List<MyContact> foundContactsByMax = contactRepository.findAllByUser(max);

        assertEquals(0, foundContactsByIvan.size());
        assertEquals(2, foundContactsByMax.size());
    }
}
