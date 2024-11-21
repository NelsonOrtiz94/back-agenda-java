package com.back.agenda.controller;

import com.back.agenda.exception.NotFoundException;
import com.back.agenda.model.Evento;
import com.back.agenda.service.EventoService;
import com.back.agenda.service.NotificationService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/evento")
@CrossOrigin(origins = "${frontend.url:http://localhost:5173}")
public class EventoController {

    private final EventoService eventoService;
    private final NotificationService notificationService;

    public EventoController(EventoService eventoService, NotificationService notificationService) {
        this.eventoService = eventoService;
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<Evento> obtenerTodosLosEventos() {
        return eventoService.obtenerTodosLosEventos();
    }

    @PostMapping
    public ResponseEntity<?> crearEvento(@Valid @RequestBody Evento evento) {
        Evento eventoCreado = eventoService.crearEvento(evento);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoCreado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evento> obtenerEventoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(eventoService.obtenerEventoPorId(id)
                .orElseThrow(() -> new NotFoundException("Evento no encontrado con ID: " + id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evento> actualizarEvento(@PathVariable Long id, @RequestBody Evento evento) {
        return ResponseEntity.ok(eventoService.actualizarEvento(id, evento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEvento(@PathVariable Long id) {
        eventoService.eliminarEvento(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/notificaciones")
    public ResponseEntity<String> enviarRecordatorioNotificacion(@PathVariable Long id) throws MessagingException, IOException {
        notificationService.sendNotificationForEvent(id);
        return ResponseEntity.ok("Notificación enviada correctamente");
    }
}

