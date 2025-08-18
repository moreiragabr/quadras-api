package app.quadras.service;

import app.quadras.entity.*;
import app.quadras.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private TimeRepository timeRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private HorarioRepository horarioRepository;
    @Autowired
    private QuadraRepository quadraRepository;

    @Transactional
    public Reserva criarReserva(List<Long> timesIds, List<Long> usuariosIds, Long horarioId, Long quadraId) {
        List<Time> times = timeRepository.findAllById(timesIds);
        List<Usuario> usuarios = usuarioRepository.findAllById(usuariosIds);
        Horario horario = horarioRepository.findById(horarioId)
                .orElseThrow(() -> new EntityNotFoundException("Horário não encontrado com ID: " + horarioId));
        Quadra quadra = quadraRepository.findById(quadraId)
                .orElseThrow(() -> new EntityNotFoundException("Quadra não encontrada com ID: " + quadraId));



        Reserva reserva = new Reserva();
        reserva.setTimesCadastrados(times);
        reserva.setUsuariosCadastrados(usuarios);
        reserva.setHorario(horario);
        reserva.setQuadra(quadra);

        return reservaRepository.save(reserva);
    }

    @Transactional(readOnly = true)
    public List<Reserva> getAll() {
        return reservaRepository.findAll();
    }
}
