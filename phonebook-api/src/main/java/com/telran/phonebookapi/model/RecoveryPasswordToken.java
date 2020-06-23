package com.telran.phonebookapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "RecoveryPasswordToken")
@Getter
@NoArgsConstructor
public class RecoveryPasswordToken {

    @Id
    private String recoveryPasswordToken;

    @OneToOne(targetEntity = User.class)
    private User user;

    public RecoveryPasswordToken(User user) {
        this.user = user;
        this.recoveryPasswordToken = UUID.randomUUID().toString();
    }
}
