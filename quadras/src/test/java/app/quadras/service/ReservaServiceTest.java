package app.quadras.service;

import app.quadras.entity.*;
import app.quadras.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private TimeRepository timeRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private HorarioRepository horarioRepository;

    @Mock
    private QuadraRepository quadraRepository;

    @InjectMocks
    private ReservaService reservaService;

    @Test
    @DisplayName("Deve criar reserva com todos os dados válidos - TESTE UNITÁRIO")
    void cenario01() {

        List<Long> timesIds = Arrays.asList(1L, 2L);
        List<Long> usuariosIds = Arrays.asList(1L, 2L);
        Long horarioId = 1L;
        Long quadraId = 1L;

        Time time1 = new Time();
        time1.setId(1L);
        Time time2 = new Time();
        time2.setId(2L);
        List<Time> times = Arrays.asList(time1, time2);

        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);

        Horario horario = new Horario();
        horario.setId(1L);

        Quadra quadra = new Quadra();
        quadra.setId(1L);

        Reserva reservaSalva = new Reserva();
        reservaSalva.setId(1L);

        when(timeRepository.findAllById(timesIds)).thenReturn(times);
        when(usuarioRepository.findAllById(usuariosIds)).thenReturn(usuarios);
        when(horarioRepository.findById(horarioId)).thenReturn(Optional.of(horario));
        when(quadraRepository.findById(quadraId)).thenReturn(Optional.of(quadra));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaSalva);

        Reserva resultado = reservaService.criarReserva(timesIds, usuariosIds, horarioId, quadraId);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(timeRepository).findAllById(timesIds);
        verify(usuarioRepository).findAllById(usuariosIds);
        verify(horarioRepository).findById(horarioId);
        verify(quadraRepository).findById(quadraId);
        verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve criar reserva com listas vazias de times e usuários - TESTE UNITÁRIO")
    void cenario02() {
        List<Long> timesIds = List.of();
        List<Long> usuariosIds = List.of();
        Long horarioId = 1L;
        Long quadraId = 1L;

        Horario horario = new Horario();
        horario.setId(1L);

        Quadra quadra = new Quadra();
        quadra.setId(1L);

        Reserva reservaSalva = new Reserva();
        reservaSalva.setId(1L);

        when(timeRepository.findAllById(timesIds)).thenReturn(List.of());
        when(usuarioRepository.findAllById(usuariosIds)).thenReturn(List.of());
        when(horarioRepository.findById(horarioId)).thenReturn(Optional.of(horario));
        when(quadraRepository.findById(quadraId)).thenReturn(Optional.of(quadra));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaSalva);

        Reserva resultado = reservaService.criarReserva(timesIds, usuariosIds, horarioId, quadraId);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(timeRepository).findAllById(timesIds);
        verify(usuarioRepository).findAllById(usuariosIds);
        verify(horarioRepository).findById(horarioId);
        verify(quadraRepository).findById(quadraId);
        verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando horário não é encontrado - TESTE UNITÁRIO")
    void cenario03() {
        List<Long> timesIds = List.of(1L);
        List<Long> usuariosIds = List.of(1L);
        Long horarioId = 999L; // ID não existente
        Long quadraId = 1L;

        Time time = new Time();
        time.setId(1L);
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(timeRepository.findAllById(timesIds)).thenReturn(List.of(time));
        when(usuarioRepository.findAllById(usuariosIds)).thenReturn(List.of(usuario));
        when(horarioRepository.findById(horarioId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> reservaService.criarReserva(timesIds, usuariosIds, horarioId, quadraId)
        );

        verify(horarioRepository).findById(horarioId);
        verify(quadraRepository, never()).findById(any());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando quadra não é encontrada - TESTE UNITÁRIO")
    void cenario04() {
        List<Long> timesIds = List.of(1L);
        List<Long> usuariosIds = List.of(1L);
        Long horarioId = 1L;
        Long quadraId = 999L;

        Time time = new Time();
        time.setId(1L);
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Horario horario = new Horario();
        horario.setId(1L);

        when(timeRepository.findAllById(timesIds)).thenReturn(List.of(time));
        when(usuarioRepository.findAllById(usuariosIds)).thenReturn(List.of(usuario));
        when(horarioRepository.findById(horarioId)).thenReturn(Optional.of(horario));
        when(quadraRepository.findById(quadraId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> reservaService.criarReserva(timesIds, usuariosIds, horarioId, quadraId)
        );

        verify(quadraRepository).findById(quadraId);
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve retornar todas as reservas - TESTE UNITÁRIO")
    void cenario05() {
        Reserva reserva1 = new Reserva();
        reserva1.setId(1L);
        Reserva reserva2 = new Reserva();
        reserva2.setId(2L);
        List<Reserva> reservas = Arrays.asList(reserva1, reserva2);

        when(reservaRepository.findAll()).thenReturn(reservas);

        List<Reserva> resultado = reservaService.getAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals(2L, resultado.get(1).getId());

        verify(reservaRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há reservas - TESTE UNITÁRIO")
    void cenario06() {
        when(reservaRepository.findAll()).thenReturn(List.of());

        List<Reserva> resultado = reservaService.getAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(reservaRepository).findAll();
    }

    @Test
    @DisplayName("Deve criar reserva mesmo quando alguns times/usuários não são encontrados - TESTE UNITÁRIO")
    void cenario07() {
        List<Long> timesIds = Arrays.asList(1L, 999L);
        List<Long> usuariosIds = Arrays.asList(1L, 888L);
        Long horarioId = 1L;
        Long quadraId = 1L;

        Time time1 = new Time();
        time1.setId(1L);
        List<Time> times = List.of(time1);

        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        List<Usuario> usuarios = List.of(usuario1);

        Horario horario = new Horario();
        horario.setId(1L);

        Quadra quadra = new Quadra();
        quadra.setId(1L);

        Reserva reservaSalva = new Reserva();
        reservaSalva.setId(1L);

        when(timeRepository.findAllById(timesIds)).thenReturn(times);
        when(usuarioRepository.findAllById(usuariosIds)).thenReturn(usuarios);
        when(horarioRepository.findById(horarioId)).thenReturn(Optional.of(horario));
        when(quadraRepository.findById(quadraId)).thenReturn(Optional.of(quadra));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaSalva);

        Reserva resultado = reservaService.criarReserva(timesIds, usuariosIds, horarioId, quadraId);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(timeRepository).findAllById(timesIds);
        verify(usuarioRepository).findAllById(usuariosIds);
        verify(horarioRepository).findById(horarioId);
        verify(quadraRepository).findById(quadraId);
        verify(reservaRepository).save(any(Reserva.class));
    }
}