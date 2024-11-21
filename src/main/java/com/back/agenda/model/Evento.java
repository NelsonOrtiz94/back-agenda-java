package com.back.agenda.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El tipo de evento no puede ser nulo")
    @Size(min = 3, max = 50, message = "El tipo de evento debe tener entre 3 y 50 caracteres")
    private String tipo;

    @NotNull(message = "El encargado no puede ser nulo")
    @Size(min = 3, max = 50, message = "El nombre del encargado debe tener entre 3 y 50 caracteres")
    private String encargado;

    @NotNull(message = "La fecha y hora no pueden ser nulas")
    private LocalDateTime fechaHora;

    @NotNull(message = "La ubicación no puede ser nula")
    @Size(min = 5, max = 100, message = "La ubicación debe tener entre 5 y 100 caracteres")
    private String ubicacion;

    @NotNull(message = "El correo del ciudadano no puede ser nulo")
    @Email(message = "El correo del ciudadano debe ser válido")
    private String emailCiudadano;

}
