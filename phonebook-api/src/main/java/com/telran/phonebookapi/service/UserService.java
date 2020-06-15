package com.telran.phonebookapi.service;

import com.telran.phonebookapi.model.User;
import com.telran.phonebookapi.persistence.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.UUID;


@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private IUserRepository userRepository;
    private EmailSenderService emailSenderService;


    public User findByConfirmationToken(String confirmationToken){
        return userRepository.findByConfirmationToken(confirmationToken);

    }

   public User findByEmail(String email){
        return userRepository.findByEmail(email);

    }
    public boolean saveUser(User userToSave){
        User byEmailFromDB = userRepository.findByEmail(userToSave.getEmail());
        if(byEmailFromDB!=null){
            return false;
        }
        userToSave.setActive(true);
        userToSave.setConfirmationToken(UUID.randomUUID().toString());
        userRepository.save(userToSave);
        if(!StringUtils.isEmpty(userToSave.getEmail())){
            String messageToSend= String.format(
                    "Hello, %s! \n"+
                            "Thank you for registration on PhoneBook Appl. Please, visit next link: //localhost:8080/activate/%s",
                    userToSave.getName(),
                    userToSave.getConfirmationToken()
            );
            emailSenderService.sendMail(userToSave.getEmail(),"Registration Confirmation", messageToSend);

        }
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User byEmail = userRepository.findByEmail(email);

        if (byEmail!=null){
            return (UserDetails) byEmail;
        }
        else {
            throw new UsernameNotFoundException(MessageFormat.format("User with email {0} cannot be found.", email));
        }
    }

    public boolean activateUser(String token) {
        User userByTokenFromDB = userRepository.findByConfirmationToken(token);

        if(userByTokenFromDB==null){
            return false;
        }
        userByTokenFromDB.setConfirmationToken(null);
        userRepository.save(userByTokenFromDB);

       return true;
    }
}
