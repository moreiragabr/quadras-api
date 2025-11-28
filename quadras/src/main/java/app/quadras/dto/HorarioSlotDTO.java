package app.quadras.dto;

import java.time.LocalTime;

public record HorarioSlotDTO(
        String horaInicio,
        String horaFim,
        boolean disponivel
) {}