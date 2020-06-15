package com.telran.phonebookapi.persistence;

import com.telran.phonebookapi.model.User;
import org.springframework.data.repository.CrudRepository;

public interface IUserRepository extends CrudRepository<User, String> {

}
