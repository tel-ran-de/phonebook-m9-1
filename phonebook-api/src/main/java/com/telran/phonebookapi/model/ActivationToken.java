package com.telran.phonebookapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "Token")
@Getter
@NoArgsConstructor
public class ActivationToken {

    @Id
    private String activationToken;

    @OneToOne(targetEntity = User.class)
    private User user;

    public ActivationToken(User user) {
        this.user = user;
        this.activationToken = UUID.randomUUID().toString();
    }
}
