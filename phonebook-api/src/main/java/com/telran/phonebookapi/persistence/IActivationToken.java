package com.telran.phonebookapi.persistence;

import com.telran.phonebookapi.model.ActivationToken;
import org.springframework.data.repository.CrudRepository;

public interface IActivationToken extends CrudRepository<ActivationToken, String> {

}
