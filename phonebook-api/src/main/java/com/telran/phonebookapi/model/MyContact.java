package com.telran.phonebookapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class MyContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Setter
    private String firstName;
    @Setter
    private String lastName;
    @Setter
    private String description;
    @Setter
    private String address;
    @Setter
    private String phone;
    @Setter
    private String email;

    @Setter
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public MyContact(String firstName, User user) {
        this.firstName = firstName;
        this.user = user;
    }
}

