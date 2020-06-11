package com.telran.phonebookapi.controller;


import com.telran.phonebookapi.config.JwtTokenUtil;
import com.telran.phonebookapi.model.JwtRequest;
import com.telran.phonebookapi.model.JwtResponse;
import com.telran.phonebookapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/*the post API gets userName and password in the body using authentication Manager.
If they are valid, a JWTToke is created using jwtTokenUtil und provided to the client*/

@RestController
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUserName(), authenticationRequest.getPassword());

        final UserDetails userDetails = userService
                .loadUserByUsername(authenticationRequest.getUserName());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        }
    }


}
