package com.telran.phonebookapi.model;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "Token")
@Getter
@NoArgsConstructor
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String confirmationToken;

    @OneToOne(targetEntity = User.class)
    private User user;

    public ConfirmationToken(User user){
        this.user=user;
        this.confirmationToken=UUID.randomUUID().toString();
    }
}
