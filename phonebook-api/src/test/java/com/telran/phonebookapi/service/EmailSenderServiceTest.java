package com.telran.phonebookapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailSenderServiceTest {
    @Mock
    EmailSenderService emailSenderService;

    @Mock
    JavaMailSender javaMailSender;
    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageArgumentCaptor;

    @Test
    public void send_mail(){
        SimpleMailMessage mailMessage=new SimpleMailMessage();

        mailMessage.setFrom("mailFrom");
        mailMessage.setTo("mailTo");
        mailMessage.setSubject("subject");
        mailMessage.setText("message");

        javaMailSender.send(mailMessage);


        verify(javaMailSender,times(1)).send(messageArgumentCaptor.capture());

        SimpleMailMessage messValue = messageArgumentCaptor.getValue();

        assertThat(messValue.getFrom()).isEqualTo("mailFrom");
        assertThat(messValue.getSubject()).isEqualTo("subject");
        assertThat(messValue.getText()).isEqualTo("message");
    }
}
