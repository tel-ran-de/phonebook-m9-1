package com.telran.phonebookapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class UserService {


    private EmailSenderService emailSenderService;

    public UserService(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    private final String MESSAGE="Thank you for registration on PhoneBook Appl. Please, visit next link:" +
            " http://localhost:8080/api/user/confirmation/" ;
    private final String SUBJ="activation of you account";


    @Value("${spring.mail.username}")
    private String mailFrom;

    public void saveUser(String email, String password) {

            emailSenderService.sendMail(email, mailFrom,
                            SUBJ,
                    MESSAGE );

        }

    }


