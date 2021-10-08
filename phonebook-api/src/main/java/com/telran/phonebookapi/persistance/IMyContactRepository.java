package com.telran.phonebookapi.persistance;

import com.telran.phonebookapi.model.MyContact;
import com.telran.phonebookapi.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IMyContactRepository extends CrudRepository<MyContact, Integer> {
    void deleteAllByUser(User user);

    List<MyContact> findAllByUser(User user);
}
