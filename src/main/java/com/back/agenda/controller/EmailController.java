package com.back.agenda.controller;

import com.back.agenda.service.GmailSender;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class EmailController {

    @Value("${google.oauth2.clientId}")
    private String clientId;

    @Value("${google.oauth2.redirectUri}")
    private String redirectUri;

    @GetMapping("/send-email")
    public String sendTestEmail(@RequestParam(required = false) String code) {
        try {
            if (code == null) {
                String authUrl = generateAuthUrl();
                return "Por favor, autentíquese con Google visitando este enlace: <a href=\"" + authUrl + "\">Autenticarse</a>";
            } else {
                GmailSender.sendEmail("destinatario@example.com", "Correo de prueba", "Este es un correo enviado desde Spring Boot con Gmail OAuth2.");
                return "Correo enviado con éxito!";
            }
        } catch (MessagingException | IOException e) {
            return "Error al enviar el correo: " + e.getMessage();
        }
    }

    private String generateAuthUrl() {
        return String.format(
                "https://accounts.google.com/o/oauth2/v2/auth?client_id=%s&redirect_uri=%s&response_type=code&scope=https://www.googleapis.com/auth/gmail.send&access_type=offline",
                clientId, redirectUri
        );
    }
}
