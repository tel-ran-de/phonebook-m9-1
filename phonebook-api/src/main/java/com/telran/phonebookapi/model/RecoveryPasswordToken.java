package com.telran.phonebookapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity()
@Getter
@NoArgsConstructor
public class RecoveryPasswordToken {

    @Id
    private String recoveryPasswordToken;

    @OneToOne(targetEntity = User.class)
    private User user;

    public RecoveryPasswordToken(User user, String recoveryPasswordToken) {
        this.user = user;
        this.recoveryPasswordToken = recoveryPasswordToken;
    }
}
