package com.back.agenda.service;

import com.back.agenda.exception.NotFoundException;
import com.back.agenda.model.Evento;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class NotificationService {

    private final EventoService eventoService;

    public NotificationService(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    public void sendNotificationForEvent(Long eventId) throws MessagingException, IOException {
        Evento evento = eventoService.obtenerEventoPorId(eventId)
                .orElseThrow(() -> new NotFoundException("Evento no encontrado con ID: " + eventId));

        String subject = "Recordatorio de Evento Importante";
        String messageBody = String.format(
                "Estimado ciudadano, le recordamos el evento importante programado:\n" +
                        "Tipo de evento: %s\nEncargado: %s\nFecha: %s\nUbicación: %s",
                evento.getTipo(), evento.getEncargado(), evento.getFechaHora(), evento.getUbicacion()
        );

        GmailSender.sendEmail(evento.getEmailCiudadano(), subject, messageBody);
    }
}

