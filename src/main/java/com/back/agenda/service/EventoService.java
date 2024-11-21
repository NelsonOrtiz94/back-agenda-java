package com.back.agenda.service;


import com.back.agenda.exception.NotFoundException;
import com.back.agenda.model.Evento;
import com.back.agenda.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public Evento crearEvento(Evento evento) {
        return eventoRepository.save(evento);
    }

    public Optional<Evento> obtenerEventoPorId(Long id) {
        return eventoRepository.findById(id);
    }

    public List<Evento> obtenerTodosLosEventos() {
        return eventoRepository.findAll();
    }

    public Evento actualizarEvento(Long id, Evento evento) {
        Evento eventoExistente = eventoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Evento no encontrado con ID: " + id));
        eventoExistente.setTipo(evento.getTipo());
        eventoExistente.setEncargado(evento.getEncargado());
        eventoExistente.setFechaHora(evento.getFechaHora());
        eventoExistente.setUbicacion(evento.getUbicacion());
        return eventoRepository.save(eventoExistente);
    }

    public void eliminarEvento(Long id) {
        if (!eventoRepository.existsById(id)) {
            throw new NotFoundException("Evento no encontrado con ID: " + id);
        }
        eventoRepository.deleteById(id);
    }
}


