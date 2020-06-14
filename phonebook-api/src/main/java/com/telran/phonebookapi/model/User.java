package com.telran.phonebookapi.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
    @Email(message = "Please provide a valid e-mail")
    @NotEmpty(message = "Please provide an e-mail")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Setter
    private String password;

    @Setter
    @Transient
    @NotBlank(message = "Password confirmation cannot be empty")
    private String password2;


    @Setter
    private boolean isActive = false;

    @Setter
    @Column(name = "confirmation_token")
    private String confirmationToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Contact> contacts = new ArrayList<>();

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

}
