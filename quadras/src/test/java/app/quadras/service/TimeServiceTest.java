package app.quadras.service;

import app.quadras.entity.Time;
import app.quadras.entity.TipoEsporte;
import app.quadras.entity.Usuario;
import app.quadras.repository.TimeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeServiceTest {

    @Mock
    private TimeRepository timeRepository;

    @InjectMocks
    private TimeService timeService;

    @Test
    @DisplayName("Deve retornar todos os times - TESTE UNITÁRIO")
    void cenario01() {
        Time time1 = new Time();
        time1.setId(1L);
        Time time2 = new Time();
        time2.setId(2L);
        List<Time> times = Arrays.asList(time1, time2);

        when(timeRepository.findAll()).thenReturn(times);

        List<Time> resultado = timeService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(timeRepository).findAll();
    }

    @Test
    @DisplayName("Deve encontrar time por ID existente - TESTE UNITÁRIO")
    void cenario02() {
        Time time = new Time();
        time.setId(1L);
        time.setNome("Time Teste");

        when(timeRepository.findById(1L)).thenReturn(Optional.of(time));

        Time resultado = timeService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Time Teste", resultado.getNome());
        verify(timeRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar time com ID inexistente - TESTE UNITÁRIO")
    void cenario03() {
        when(timeRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> timeService.findById(999L)
        );

        assertNotNull(exception);
        verify(timeRepository).findById(999L);
    }

    @Test
    @DisplayName("Deve salvar time com sucesso - TESTE UNITÁRIO")
    void cenario04() {
        Time time = new Time();
        time.setNome("Novo Time");

        Time timeSalvo = new Time();
        timeSalvo.setId(1L);
        timeSalvo.setNome("Novo Time");

        when(timeRepository.save(time)).thenReturn(timeSalvo);

        Time resultado = timeService.save(time);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Novo Time", resultado.getNome());
        verify(timeRepository).save(time);
    }

    @Test
    @DisplayName("Deve deletar time sem jogadores - TESTE UNITÁRIO")
    void cenario05() {
        Time time = new Time();
        time.setId(1L);
        time.setJogadores(null);

        when(timeRepository.findById(1L)).thenReturn(Optional.of(time));
        when(timeRepository.saveAndFlush(time)).thenReturn(time);
        doNothing().when(timeRepository).delete(time);

        timeService.delete(1L);

        verify(timeRepository).findById(1L);
        verify(timeRepository).saveAndFlush(time);
        verify(timeRepository).delete(time);
    }

    @Test
    @DisplayName("Deve deletar time limpando lista de jogadores - TESTE UNITÁRIO")
    void cenario06() {
        Time time = new Time();
        time.setId(1L);

        Usuario jogador1 = new Usuario();
        jogador1.setId(1L);
        Usuario jogador2 = new Usuario();
        jogador2.setId(2L);

        List<Usuario> jogadores = new ArrayList<>();
        jogadores.add(jogador1);
        jogadores.add(jogador2);
        time.setJogadores(jogadores);

        when(timeRepository.findById(1L)).thenReturn(Optional.of(time));
        when(timeRepository.saveAndFlush(time)).thenReturn(time);
        doNothing().when(timeRepository).delete(time);

        timeService.delete(1L);

        assertNotNull(time.getJogadores());
        assertTrue(time.getJogadores().isEmpty());
        verify(timeRepository).findById(1L);
        verify(timeRepository).saveAndFlush(time);
        verify(timeRepository).delete(time);
    }


    @Test
    @DisplayName("Deve atualizar nome e tipo esporte do time - TESTE UNITÁRIO")
    void cenario07() {
        Time timeExistente = new Time();
        timeExistente.setId(1L);
        timeExistente.setNome("Time Antigo");
        timeExistente.setTipoEsporte(TipoEsporte.FUTSAL);

        Time timeAtualizacao = new Time();
        timeAtualizacao.setNome("Time Novo");
        timeAtualizacao.setTipoEsporte(TipoEsporte.VOLEI);

        when(timeRepository.findById(1L)).thenReturn(Optional.of(timeExistente));
        when(timeRepository.save(timeExistente)).thenReturn(timeExistente);

        Time resultado = timeService.update(1L, timeAtualizacao);

        assertNotNull(resultado);
        assertEquals("Time Novo", timeExistente.getNome());
        assertEquals(TipoEsporte.VOLEI, timeExistente.getTipoEsporte());
        verify(timeRepository).findById(1L);
        verify(timeRepository).save(timeExistente);
    }

    @Test
    @DisplayName("Deve manter valores originais ao atualizar com campos nulos - TESTE UNITÁRIO")
    void cenario08() {
        Time timeExistente = new Time();
        timeExistente.setId(1L);
        timeExistente.setNome("Time Original");
        timeExistente.setTipoEsporte(TipoEsporte.BASQUETE);

        Time timeAtualizacao = new Time();

        when(timeRepository.findById(1L)).thenReturn(Optional.of(timeExistente));
        when(timeRepository.save(timeExistente)).thenReturn(timeExistente);

        Time resultado = timeService.update(1L, timeAtualizacao);

        assertNotNull(resultado);
        assertEquals("Time Original", timeExistente.getNome());
        assertEquals(TipoEsporte.BASQUETE, timeExistente.getTipoEsporte());
        verify(timeRepository).findById(1L);
        verify(timeRepository).save(timeExistente);
    }

    @Test
    @DisplayName("Deve ignorar campos vazios ou em branco na atualização - TESTE UNITÁRIO")
    void cenario09() {

        Time timeExistente = new Time();
        timeExistente.setId(1L);
        timeExistente.setNome("Time Original");
        timeExistente.setTipoEsporte(TipoEsporte.FUTSAL);

        Time timeAtualizacao = new Time();
        timeAtualizacao.setNome("");
        timeAtualizacao.setTipoEsporte(null);

        when(timeRepository.findById(1L)).thenReturn(Optional.of(timeExistente));
        when(timeRepository.save(timeExistente)).thenReturn(timeExistente);

        Time resultado = timeService.update(1L, timeAtualizacao);

        assertNotNull(resultado);
        assertEquals("Time Original", timeExistente.getNome());
        assertEquals(TipoEsporte.FUTSAL, timeExistente.getTipoEsporte());
        verify(timeRepository).findById(1L);
        verify(timeRepository).save(timeExistente);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar time inexistente - TESTE UNITÁRIO")
    void cenario10() {
        Time timeAtualizacao = new Time();
        timeAtualizacao.setNome("Time Novo");

        when(timeRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> timeService.update(999L, timeAtualizacao)
        );

        assertNotNull(exception);
        verify(timeRepository).findById(999L);
        verify(timeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar time inexistente - TESTE UNITÁRIO")
    void cenario11() {
        when(timeRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> timeService.delete(999L)
        );

        assertNotNull(exception);
        verify(timeRepository).findById(999L);
        verify(timeRepository, never()).saveAndFlush(any());
        verify(timeRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Deve atualizar apenas nome quando tipoEsporte é nulo - TESTE UNITÁRIO")
    void cenario12() {
        Time timeExistente = new Time();
        timeExistente.setId(1L);
        timeExistente.setNome("Time Antigo");
        timeExistente.setTipoEsporte(TipoEsporte.FUTSAL);

        Time timeAtualizacao = new Time();
        timeAtualizacao.setNome("Time Novo");
        timeAtualizacao.setTipoEsporte(null);

        when(timeRepository.findById(1L)).thenReturn(Optional.of(timeExistente));
        when(timeRepository.save(timeExistente)).thenReturn(timeExistente);

        Time resultado = timeService.update(1L, timeAtualizacao);

        assertNotNull(resultado);
        assertEquals("Time Novo", timeExistente.getNome());
        assertEquals(TipoEsporte.FUTSAL, timeExistente.getTipoEsporte());
        verify(timeRepository).findById(1L);
        verify(timeRepository).save(timeExistente);
    }

    @Test
    @DisplayName("Deve atualizar apenas tipoEsporte quando nome é nulo - TESTE UNITÁRIO")
    void cenario13() {
        Time timeExistente = new Time();
        timeExistente.setId(1L);
        timeExistente.setNome("Time Antigo");
        timeExistente.setTipoEsporte(TipoEsporte.FUTSAL);

        Time timeAtualizacao = new Time();
        timeAtualizacao.setNome(null);
        timeAtualizacao.setTipoEsporte(TipoEsporte.VOLEI);

        when(timeRepository.findById(1L)).thenReturn(Optional.of(timeExistente));
        when(timeRepository.save(timeExistente)).thenReturn(timeExistente);

        Time resultado = timeService.update(1L, timeAtualizacao);

        assertNotNull(resultado);
        assertEquals("Time Antigo", timeExistente.getNome());
        assertEquals(TipoEsporte.VOLEI, timeExistente.getTipoEsporte());
        verify(timeRepository).findById(1L);
        verify(timeRepository).save(timeExistente);
    }
}