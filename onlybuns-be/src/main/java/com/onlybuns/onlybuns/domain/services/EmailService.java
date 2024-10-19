package com.onlybuns.onlybuns.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onlybuns.onlybuns.core.misc.EmailParser;
import com.onlybuns.onlybuns.domain.models.EmailAuthentication;
import com.onlybuns.onlybuns.infrastructure.repositories.EmailRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailService {
    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private EmailParser emailParser;

    public EmailAuthentication findByEmail(String email) {
        return emailRepository.findByEmail(email);
    }

    @Transactional
    public EmailAuthentication send(String email) throws MessagingException {
        EmailAuthentication emailAuthentication = new EmailAuthentication();
        emailAuthentication.setEmail(email);
        emailAuthentication.setCreatedAt(LocalDateTime.now());
        emailAuthentication.setToken(UUID.randomUUID().toString());
        emailAuthentication.generateEmailLink();

        sendVerificationEmail(email, emailAuthentication.getEmailLink());

        return emailRepository.save(emailAuthentication);
    }

    private void sendVerificationEmail(String email, String emailLink) throws MessagingException {
        String htmlContent = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Email Verification</title>\n" +
            "    <style>\n" +
            "        body {\n" +
            "            font-family: Arial, sans-serif;\n" +
            "            background-color: #f4f4f4;\n" +
            "            margin: 0;\n" +
            "            padding: 0;\n" +
            "        }\n" +
            "        .container {\n" +
            "            max-width: 600px;\n" +
            "            margin: 50px auto;\n" +
            "            background-color: #ffffff;\n" +
            "            border-radius: 8px;\n" +
            "            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\n" +
            "            overflow: hidden;\n" +
            "        }\n" +
            "        .header {\n" +
            "            background-color: #007bff;\n" +
            "            color: #ffffff;\n" +
            "            padding: 20px;\n" +
            "            text-align: center;\n" +
            "        }\n" +
            "        .content {\n" +
            "            padding: 20px;\n" +
            "        }\n" +
            "        .content h1 {\n" +
            "            font-size: 24px;\n" +
            "            margin: 0 0 20px;\n" +
            "        }\n" +
            "        .content p {\n" +
            "            line-height: 1.6;\n" +
            "            color: #333333;\n" +
            "        }\n" +
            "        .button {\n" +
            "            display: inline-block;\n" +
            "            background-color: #28a745;\n" +
            "            color: #ffffff;\n" +
            "            padding: 15px 30px;\n" +
            "            text-decoration: none;\n" +
            "            border-radius: 5px;\n" +
            "            margin-top: 20px;\n" +
            "        }\n" +
            "        .footer {\n" +
            "            text-align: center;\n" +
            "            padding: 20px;\n" +
            "            font-size: 12px;\n" +
            "            color: #777777;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"container\">\n" +
            "        <div class=\"header\">\n" +
            "            <h1>Welcome to OnlyBuns!</h1>\n" +
            "        </div>\n" +
            "        <div class=\"content\">\n" +
            "            <h1>Email Verification</h1>\n" +
            "            <p>Hello,</p>\n" +
            "            <p>Thank you for signing up with OnlyBuns! To complete your registration, please verify your email address by clicking the button below:</p>\n" +
            "            <b href=\"" + emailLink + "\" class=\"button\">Verify Your Email</b>\n" +
            "            <p>If you didn't create an account, no further action is required.</p>\n" +
            "            <p>Thank you, and welcome aboard!</p>\n" +
            "        </div>\n" +
            "        <div class=\"footer\">\n" +
            "            <p>© 2024 OnlyBuns. All rights reserved.</p>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";

        // Create the message with HTML content
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("[NOREPLY] Onlybuns - Verify Your Account");
        helper.setText(htmlContent, true);

        try {
            mailSender.send(message);
        } catch(Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }

    public boolean isValidEmail(String email) {
        return emailParser.isValidEmail(email);
    }
}
