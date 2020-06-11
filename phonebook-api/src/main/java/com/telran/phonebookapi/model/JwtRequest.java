package com.telran.phonebookapi.model;
/*This class is required for storing the username and pass we recieve from the client(browser) */


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/*this class for storing username and pas from client*/
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class JwtRequest implements Serializable {

    private String userName;
    private String password;
}
