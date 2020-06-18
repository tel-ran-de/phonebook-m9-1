package com.telran.phonebookapi.model;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.UUID;

@Entity(name = "Token")
@Getter
@NoArgsConstructor
public class ConfirmationToken {

    @Id
    private String confirmationToken;

    @OneToOne(targetEntity = User.class)
    private User user;

    public ConfirmationToken(User user){
        this.user=user;
        this.confirmationToken=UUID.randomUUID().toString();
    }
}
