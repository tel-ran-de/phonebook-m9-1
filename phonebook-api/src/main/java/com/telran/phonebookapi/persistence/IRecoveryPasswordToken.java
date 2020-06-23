package com.telran.phonebookapi.persistence;

import com.telran.phonebookapi.model.RecoveryPasswordToken;
import org.springframework.data.repository.CrudRepository;

public interface IRecoveryPasswordToken extends CrudRepository<RecoveryPasswordToken, String> {

}
