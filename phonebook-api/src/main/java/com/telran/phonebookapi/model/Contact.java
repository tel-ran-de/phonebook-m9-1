package com.telran.phonebookapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Setter
    private String name;
    @Setter
    private String lastName;
    @Setter
    private String description;
    @ElementCollection
    private List<String> emails;

    @ManyToOne
//    @OnDelete(action = OnDeleteAction.CASCADE)
            User user;

    @OneToMany(mappedBy = "contact", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<PhoneNumber> numbers = new ArrayList<>();

    @OneToMany(mappedBy = "contact", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    public Contact(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public void addPhoneNumber(PhoneNumber phoneNumber) {
        numbers.add(phoneNumber);
    }

    public void addAddress(Address address) {
        addresses.add(address);
    }
}
