package com.telran.phonebookapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*this class for creating a response with JWT to be returned to client*/

@AllArgsConstructor
@Getter
public class JwtResponse {

    private final String jwtToken;
}
