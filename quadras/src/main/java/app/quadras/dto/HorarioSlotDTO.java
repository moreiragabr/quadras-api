package app.quadras.dto;

import java.time.LocalTime;

public record HorarioSlotDTO(
        String horaInicio, // Usando String para simplicidade na comunicação com Angular
        String horaFim,    // Usando String para simplicidade na comunicação com Angular
        boolean disponivel
) {}