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
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private UsuarioRepository usuarioRepository; // Usado para obter o objeto Usuario logado

    /**
     * Endpoint para listar os slots disponíveis de um CAMPO em uma data específica.
     * @param campoId O ID do campo.
     * @param data A data desejada no formato YYYY-MM-DD.
     * @return Lista de HorarioSlotDTO com a disponibilidade.
     */
    // 💥 MUDANÇA: Usa /campo/{campoId} em vez de /quadra/{quadraId}
    @GetMapping("/slots/campo/{campoId}")
    public ResponseEntity<List<HorarioSlotDTO>> getSlotsDisponiveis(
            @PathVariable Long campoId,
            @RequestParam("data") LocalDate data) {

        List<HorarioSlotDTO> slots = reservaService.getHorariosDisponiveis(campoId, data);
        return ResponseEntity.ok(slots);
    }

    /**
     * Endpoint para criar uma nova reserva para um CAMPO.
     * @param reservaDto DTO com o ID do campo e o início da reserva.
     * @return 201 Created ou 400 Bad Request em caso de falha.
     */
    @PostMapping
    public ResponseEntity<Void> criarReserva(@RequestBody ReservaRequestDTO reservaDto) {

        // 1. Obtém o usuário logado (Autenticado)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Se você estiver usando o 'email' como principal, a lógica será:
        String userEmail = authentication.getName();

        Usuario usuarioLogado = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuário logado não encontrado no banco de dados."));

        try {
            // 2. Chama o serviço para salvar a reserva
            // 💥 MUDANÇA: Passa reservaDto.campoId()
            reservaService.salvarReserva(reservaDto.campoId(), reservaDto.inicioReserva(), usuarioLogado);
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (IllegalStateException e) {
            // Ex: Horário já reservado
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}