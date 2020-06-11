package com.telran.phonebookapi.service;

import com.telran.phonebookapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService  implements UserDetailsService {
   @Autowired
   UserRepository userRepository;

   @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= userRepository.findByUserName(username);

        if(user==null){
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public User findUserById (int userId){
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());

    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public boolean addUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getName()); //should search by token?

        if (userFromDB != null) {
            return false;
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.isActive(true);
        userRepository.save(user);
        return true;
    }

    public boolean deleteUserById(int userId) { // why not Long
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }



}
