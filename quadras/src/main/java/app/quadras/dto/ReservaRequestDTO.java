package app.quadras.dto;

import java.time.LocalDateTime;

public record ReservaRequestDTO(
        Long campoId, // 💥 NOVO: ID do Campo específico
        LocalDateTime inicioReserva
) {}