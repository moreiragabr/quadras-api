package app.quadras.service;

import app.quadras.entity.Quadra;
import app.quadras.entity.Reserva;
import app.quadras.entity.Usuario; // Assumindo que você tem essa entidade
import app.quadras.entity.Campo; // Importa a entidade Campo
import app.quadras.dto.HorarioSlotDTO;
import app.quadras.entity.HorarioDia;
import app.quadras.repository.QuadraRepository;
import app.quadras.repository.ReservaRepository;
import app.quadras.repository.CampoRepository; // Assumindo que você tem o repositório para Campo

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    @Autowired
    private QuadraRepository quadraRepository;
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private CampoRepository campoRepository;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");


    public List<HorarioSlotDTO> getHorariosDisponiveis(Long campoId, LocalDate data) {

        Campo campo = campoRepository.findById(campoId)
                .orElseThrow(() -> new EntityNotFoundException("Campo não encontrado."));

        Quadra quadra = campo.getQuadra();

        long dayOfWeekValue = data.getDayOfWeek().getValue();

        HorarioDia horarioDoDia = quadra.getHorariosDeFuncionamento().stream()
                .filter(h -> h.getDayOfWeek() != null && h.getDayOfWeek() == dayOfWeekValue)
                .findFirst()
                .orElse(null);

        if (horarioDoDia == null || !horarioDoDia.getIsOpen()) {
            return List.of(); // Quadra fechada ou horário não definido
        }

        LocalTime horaAbertura = LocalTime.parse(horarioDoDia.getOpenTime(), TIME_FORMATTER);
        LocalTime horaFechamento = LocalTime.parse(horarioDoDia.getCloseTime(), TIME_FORMATTER);

        List<Reserva> reservasOcupadas = reservaRepository.findByCampoAndData(campo, data);

        Set<LocalTime> horasOcupadas = reservasOcupadas.stream()
                .map(r -> r.getInicioReserva().toLocalTime())
                .collect(Collectors.toSet());

        List<HorarioSlotDTO> slots = new ArrayList<>();
        LocalTime horaAtual = horaAbertura;

        while (horaAtual.isBefore(horaFechamento)) {
            LocalTime proximaHora = horaAtual.plus(1, ChronoUnit.HOURS);

            if (proximaHora.isAfter(horaFechamento)) {
                break;
            }

            boolean isDisponivel = !horasOcupadas.contains(horaAtual);

            slots.add(new HorarioSlotDTO(
                    horaAtual.format(TIME_FORMATTER),
                    proximaHora.format(TIME_FORMATTER),
                    isDisponivel
            ));

            horaAtual = proximaHora;
        }

        return slots;
    }


    public void salvarReserva(Long campoId, LocalDateTime inicioReserva, Usuario usuario) {
        Campo campo = campoRepository.findById(campoId)
                .orElseThrow(() -> new EntityNotFoundException("Campo não encontrado."));

        if (reservaRepository.findByCampoAndInicioReserva(campo, inicioReserva).isPresent()) {
            throw new IllegalStateException("Horário já reservado. Conflito de agendamento.");
        }

        Reserva novaReserva = new Reserva();
        novaReserva.setCampo(campo); // Define o Campo
        novaReserva.setUsuario(usuario);
        novaReserva.setInicioReserva(inicioReserva);

        reservaRepository.save(novaReserva);
    }
}