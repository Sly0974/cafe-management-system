package com.example.cafemanagementsystem.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailUtils {

    private final JavaMailSender emailSender;

    private final Environment environment;


    @Autowired
    public EmailUtils(JavaMailSender emailSender, Environment environment) {
        this.emailSender = emailSender;
        this.environment = environment;
    }

    public void sendSimpleMessage(String to, String subject, String text, List<String> list) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(environment.getProperty("spring.mail.username"));
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        if (list != null && list.size() > 0) {
            message.setCc(list.toArray(list.toArray(String[]::new)));
        }
        emailSender.send(message);
    }
}
