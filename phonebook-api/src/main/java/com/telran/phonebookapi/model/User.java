package com.telran.phonebookapi.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Users")
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

    @Setter
    private boolean isActive = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Contact> contacts = new ArrayList<>();

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

}
