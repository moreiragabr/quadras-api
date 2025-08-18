package app.quadras.controller;

import app.quadras.entity.Reserva;
import app.quadras.entity.ReservaDto;
import app.quadras.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public ResponseEntity<List<Reserva>> getAllReservas() {
        List<Reserva> reservas = reservaService.getAll();
        return ResponseEntity.ok(reservas);
    }

    @PostMapping
    public ResponseEntity<Reserva> createReserva(@RequestBody ReservaDto reservaDto) {
        try {
            Reserva novaReserva = reservaService.criarReserva(
                    reservaDto.getTimesIds(),
                    reservaDto.getUsuariosIds(),
                    reservaDto.getHorarioId(),
                    reservaDto.getQuadraId()
            );
            return new ResponseEntity<>(novaReserva, HttpStatus.CREATED);
        } catch (Exception e) {

            return ResponseEntity.badRequest().build();
        }
    }
}
