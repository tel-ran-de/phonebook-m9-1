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
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Setter
    private String zipCode;
    @Setter
    private String country;
    @Setter
    private String city;
    @Setter
    private String address;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    Contact contact;

    public Address(String address, Contact contact) {
        this.address = address;
        this.contact = contact;
    }
}
