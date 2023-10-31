package com.example.cafemanagementsystem.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class EmailUtils {

    private final JavaMailSender emailSender;

    private final Environment environment;

    private static final String FORGOT_PASSWD_TEMPLATE =
            """
                    <h3>Your Login details for Cafe Management System</h3>
                    <p><b>Email: </b> %s <br><b>Password: </b> %s <br>
                    <a href=\"http://localhost:4200/\">Click here to login</a></p>
                    """;

    @Autowired
    public EmailUtils(@NotNull final JavaMailSender emailSender,
                      @NotNull final Environment environment) {
        this.emailSender = emailSender;
        this.environment = environment;
    }

    public void sendSimpleMessage(@NotNull final String to,
                                  @NotNull final String subject,
                                  @NotNull final String text,
                                  final List<String> list) {
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

    public void forgotMail(@NotNull final String to,
                           @NotNull final String subject,
                           @NotNull final String password) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(environment.getProperty("spring.mail.username"));
        helper.setTo(to);
        helper.setSubject(subject);
        message.setContent(String.format(FORGOT_PASSWD_TEMPLATE, to, password), "text/html");
        emailSender.send(message);
    }
}
