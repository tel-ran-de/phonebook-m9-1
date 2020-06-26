package com.telran.phonebookapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailSenderServiceTest {
    @InjectMocks
    EmailSenderService emailSenderService;

    @Mock
    JavaMailSender javaMailSender;
    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageArgumentCaptor;

    @Value("${spring.mail.username}")
    String mailFrom = "mailFrom";


    @Test
    public void send_mail2() {

        String mailTo = "mailTo";
        String text = "text";
        String subject = "subject";


        emailSenderService.sendMail(mailTo, mailFrom, subject, text);

        verify(javaMailSender, times(1)).send(messageArgumentCaptor.capture());

        SimpleMailMessage capturedMessage = messageArgumentCaptor.getValue();

        assertEquals(mailFrom, capturedMessage.getFrom());
        assertEquals(mailTo, Objects.requireNonNull(capturedMessage.getTo())[0]);
        assertEquals(text, capturedMessage.getText());
        assertEquals(subject, capturedMessage.getSubject());
    }
}
