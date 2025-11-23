package app.quadras.service;

import app.quadras.entity.*;
import app.quadras.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;
//    @Autowired
//    private TimeRepository timeRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private HorarioRepository horarioRepository;
    @Autowired
    private QuadraRepository quadraRepository;

//    public Reserva criarReserva(List<Long> timesIds, List<Long> usuariosIds, Long horarioId, Long quadraId) {
//        List<Time> times = timeRepository.findAllById(timesIds);
//        List<Usuario> usuarios = usuarioRepository.findAllById(usuariosIds);
//        HorarioDia horario = horarioRepository.findById(horarioId).orElseThrow();
//        Quadra quadra = quadraRepository.findById(quadraId).orElseThrow();
//
//        Reserva reserva = new Reserva();
//        reserva.setTimesCadastrados(times);
//        reserva.setUsuariosCadastrados(usuarios);
//        reserva.setHorario(horario);
//        reserva.setQuadra(quadra);
//
//        return reservaRepository.save(reserva);
//    }

    public List<Reserva> getAll() {
        return reservaRepository.findAll();
    }
}