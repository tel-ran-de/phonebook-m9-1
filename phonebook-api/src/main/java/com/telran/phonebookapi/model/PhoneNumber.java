package com.telran.phonebookapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class PhoneNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Setter
    private String countryCode;
    @Setter
    private String number;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    Contact contact;

    public PhoneNumber(String number, Contact contact) {
        this.number = number;
        this.contact = contact;
    }
}
