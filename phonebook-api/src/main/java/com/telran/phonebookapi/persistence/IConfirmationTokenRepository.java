package com.telran.phonebookapi.persistence;

import com.telran.phonebookapi.model.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;

public interface IConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {

}
