package app.quadras.dto;

import app.quadras.entity.Reserva;
import java.time.format.DateTimeFormatter;

public record ReservaDTO(Long id, String data, String hora) {
    public static ReservaDTO fromReserva(Reserva reserva) {
        DateTimeFormatter dataFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter horaFmt = DateTimeFormatter.ofPattern("HH:mm");

        return new ReservaDTO(
                reserva.getId(),
                reserva.getInicioReserva().format(dataFmt),
                reserva.getInicioReserva().format(horaFmt)
        );
    }
}
