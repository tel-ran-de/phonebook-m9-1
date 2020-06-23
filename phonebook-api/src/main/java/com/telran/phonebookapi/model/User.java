package com.telran.phonebookapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity (name = "Users")
@Getter
@NoArgsConstructor
public class User {

    @Setter
    private String name;
    @Setter
    private String lastName;
    @Id
    private String email;
    @Setter
    private String password;

    private boolean isActive;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Contact> contacts = new ArrayList<>();

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        isActive = false;
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

}
