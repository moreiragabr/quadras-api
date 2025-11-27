package app.quadras.controller;

import app.quadras.dto.HorarioSlotDTO;
import app.quadras.dto.ReservaRequestDTO;
import app.quadras.entity.Usuario; // Assumindo que você tem essa entidade
import app.quadras.service.ReservaService;
import app.quadras.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin("*")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/slots/campo/{campoId}")
    public ResponseEntity<List<HorarioSlotDTO>> getSlotsDisponiveis(
            @PathVariable Long campoId,
            @RequestParam("data") LocalDate data) {

        List<HorarioSlotDTO> slots = reservaService.getHorariosDisponiveis(campoId, data);
        return ResponseEntity.ok(slots);
    }

    @PostMapping
    public ResponseEntity<Void> criarReserva(@RequestBody ReservaRequestDTO reservaDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userEmail = authentication.getName();

        Usuario usuarioLogado = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuário logado não encontrado no banco de dados."));

        try {
            reservaService.salvarReserva(reservaDto.campoId(), reservaDto.inicioReserva(), usuarioLogado);
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}