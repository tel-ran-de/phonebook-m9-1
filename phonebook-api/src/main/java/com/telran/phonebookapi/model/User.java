package com.telran.phonebookapi.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @NotBlank(message = "Username cannot be empty")
    @Size(min=1, max=10)
    @Setter
    private String name;

    @Setter
    @NotBlank(message = "User lastname cannot be empty")
    @Size(min=2,max=20)
    private String lastName;

    @Id
    private String email;

    @Setter
    private String password;

    @Setter
    private boolean isActive = false;


    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Contact> contacts = new ArrayList<>();

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        isActive=false;
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

}
