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
    private CampoRepository campoRepository; // NOVO: Repositório para Campos

    // Formato esperado para as strings de tempo (ex: "08:00")
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Gera e retorna a lista de horários (slots de 1 hora) disponíveis para um CAMPO específico.
     * @param campoId O ID do campo.
     * @param data A data desejada para a reserva.
     * @return Lista de HorarioSlotDTO com a disponibilidade.
     */
    public List<HorarioSlotDTO> getHorariosDisponiveis(Long campoId, LocalDate data) {

        // 1. Busca o Campo e a Quadra associada (para obter o horário de funcionamento)
        Campo campo = campoRepository.findById(campoId)
                .orElseThrow(() -> new EntityNotFoundException("Campo não encontrado."));

        Quadra quadra = campo.getQuadra();

        // 2. Encontra o HorarioDia de funcionamento (regra está na Quadra)
        long dayOfWeekValue = data.getDayOfWeek().getValue();

        HorarioDia horarioDoDia = quadra.getHorariosDeFuncionamento().stream()
                .filter(h -> h.getDayOfWeek() != null && h.getDayOfWeek() == dayOfWeekValue)
                .findFirst()
                .orElse(null);

        // Verifica se a quadra está aberta
        if (horarioDoDia == null || !horarioDoDia.getIsOpen()) {
            return List.of(); // Quadra fechada ou horário não definido
        }

        LocalTime horaAbertura = LocalTime.parse(horarioDoDia.getOpenTime(), TIME_FORMATTER);
        LocalTime horaFechamento = LocalTime.parse(horarioDoDia.getCloseTime(), TIME_FORMATTER);

        // 3. Busca reservas existentes para ESTE CAMPO ESPECÍFICO
        List<Reserva> reservasOcupadas = reservaRepository.findByCampoAndData(campo, data);

        Set<LocalTime> horasOcupadas = reservasOcupadas.stream()
                .map(r -> r.getInicioReserva().toLocalTime())
                .collect(Collectors.toSet());

        // 4. Gera todos os slots de 1 hora
        List<HorarioSlotDTO> slots = new ArrayList<>();
        LocalTime horaAtual = horaAbertura;

        // Loop a cada 1 hora
        while (horaAtual.isBefore(horaFechamento)) {
            LocalTime proximaHora = horaAtual.plus(1, ChronoUnit.HOURS);

            // Se o próximo slot exceder o horário de fechamento, pare o loop
            if (proximaHora.isAfter(horaFechamento)) {
                break;
            }

            // Verifica se a hora de início atual está reservada
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


    /**
     * Salva uma nova reserva após validações, associando-a ao Campo.
     * @param campoId ID do campo.
     * @param inicioReserva Data e hora exata de início da reserva.
     * @param usuario Usuario logado que está efetuando a reserva.
     * @throws IllegalStateException Se o horário já estiver reservado.
     */
    public void salvarReserva(Long campoId, LocalDateTime inicioReserva, Usuario usuario) {
        // 1. Busca o Campo
        Campo campo = campoRepository.findById(campoId)
                .orElseThrow(() -> new EntityNotFoundException("Campo não encontrado."));

        // 2. Validação de Conflito: Verifica se já existe uma reserva para o horário exato e este CAMPO
        if (reservaRepository.findByCampoAndInicioReserva(campo, inicioReserva).isPresent()) {
            throw new IllegalStateException("Horário já reservado. Conflito de agendamento.");
        }

        // 3. Cria a nova entidade Reserva
        Reserva novaReserva = new Reserva();
        novaReserva.setCampo(campo); // Define o Campo
        novaReserva.setUsuario(usuario);
        novaReserva.setInicioReserva(inicioReserva);

        // 4. Salva no banco de dados
        reservaRepository.save(novaReserva);
    }
}