package com.telran.phonebookapi.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailSenderService {

    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String username;

    @Async
    public void sendMail(String mailTo, String subject, String message){
        SimpleMailMessage mailMessage=new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo(mailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        javaMailSender.send(mailMessage);
    }
}
