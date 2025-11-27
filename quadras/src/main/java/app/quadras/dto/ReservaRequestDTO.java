package app.quadras.dto;

import java.time.LocalDateTime;

public record ReservaRequestDTO(
        Long campoId,
        LocalDateTime inicioReserva
) {}