package app.quadras.service;

import app.quadras.entity.Horario;
import app.quadras.entity.Quadra;
import app.quadras.repository.HorarioRepository;
import jakarta.persistence.EntityNotFoundException;
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
public class HorarioServiceTest {
    @Mock
    private HorarioRepository repository;

    @InjectMocks
    private HorarioService horarioService;

    @Test
    @DisplayName("Deve retornar todos os horários - TESTE UNITÁRIO")
    void cenario01() {
        Horario horario1 = new Horario();
        horario1.setId(1L);
        Horario horario2 = new Horario();
        horario2.setId(2L);

        when(repository.findAll()).thenReturn(Arrays.asList(horario1, horario2));

        List<Horario> resultado = horarioService.findAll();

        assertEquals(2, resultado.size());
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Deve encontrar horário por ID existente - TESTE UNITÁRIO")
    void cenario02() {
        Horario horario = new Horario();
        horario.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(horario));

        Horario resultado = horarioService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar horário com ID inexistente - TESTE UNITÁRIO")
    void cenario03() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> horarioService.findById(999L)
        );

        assertEquals("Horário não encontrado com id 999", exception.getMessage());
        verify(repository).findById(999L);
    }

    @Test
    @DisplayName("Deve salvar horário com quadra - TESTE UNITÁRIO")
    void cenario04() {
        Quadra quadra = new Quadra();
        quadra.setId(1L);

        Horario horario = new Horario();
        horario.setQuadra(quadra);

        Horario horarioSalvo = new Horario();
        horarioSalvo.setId(1L);

        when(repository.save(horario)).thenReturn(horarioSalvo);

        Horario resultado = horarioService.save(horario);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(repository).save(horario);
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar horário sem quadra - TESTE UNITÁRIO")
    void cenario05() {
        Horario horario = new Horario();

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> horarioService.save(horario)
        );

        assertEquals("Horário precisa de uma quadra", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve deletar horário existente - TESTE UNITÁRIO")
    void cenario06() {
        Horario horario = new Horario();
        horario.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(horario));
        doNothing().when(repository).delete(horario);

        horarioService.delete(1L);

        verify(repository).findById(1L);
        verify(repository).delete(horario);
    }

    @Test
    @DisplayName("Deve atualizar todos os campos do horário - TESTE UNITÁRIO")
    void cenario07() {
        Quadra quadra = new Quadra();
        quadra.setId(2L);

        Horario horarioExistente = new Horario();
        horarioExistente.setId(1L);
        horarioExistente.setHorario("18:00");
        horarioExistente.setData("2023-01-01");

        Horario horarioAtualizacao = new Horario();
        horarioAtualizacao.setHorario("20:00");
        horarioAtualizacao.setData("2023-01-02");
        horarioAtualizacao.setQuadra(quadra);

        when(repository.findById(1L)).thenReturn(Optional.of(horarioExistente));
        when(repository.save(horarioExistente)).thenReturn(horarioExistente);

        Horario resultado = horarioService.update(1L, horarioAtualizacao);

        assertNotNull(resultado);
        assertEquals("20:00", horarioExistente.getHorario());
        assertEquals("2023-01-02", horarioExistente.getData());
        assertEquals(quadra, horarioExistente.getQuadra());

        verify(repository).findById(1L);
        verify(repository).save(horarioExistente);
    }

    @Test
    @DisplayName("Deve manter valores originais ao atualizar com campos nulos - TESTE UNITÁRIO")
    void cenario08() {
        Horario horarioExistente = new Horario();
        horarioExistente.setId(1L);
        horarioExistente.setHorario("18:00");
        horarioExistente.setData("2023-01-01");

        Horario horarioAtualizacao = new Horario();

        when(repository.findById(1L)).thenReturn(Optional.of(horarioExistente));
        when(repository.save(horarioExistente)).thenReturn(horarioExistente);

        Horario resultado = horarioService.update(1L, horarioAtualizacao);

        assertNotNull(resultado);
        assertEquals("18:00", horarioExistente.getHorario());
        assertEquals("2023-01-01", horarioExistente.getData());

        verify(repository).findById(1L);
        verify(repository).save(horarioExistente);
    }

    @Test
    @DisplayName("Deve ignorar campos vazios ou em branco na atualização - TESTE UNITÁRIO")
    void cenario09() {
        Horario horarioExistente = new Horario();
        horarioExistente.setId(1L);
        horarioExistente.setHorario("18:00");
        horarioExistente.setData("2023-01-01");

        Horario horarioAtualizacao = new Horario();
        horarioAtualizacao.setHorario("");
        horarioAtualizacao.setData("   ");

        when(repository.findById(1L)).thenReturn(Optional.of(horarioExistente));
        when(repository.save(horarioExistente)).thenReturn(horarioExistente);

        Horario resultado = horarioService.update(1L, horarioAtualizacao);

        assertNotNull(resultado);
        assertEquals("18:00", horarioExistente.getHorario());
        assertEquals("2023-01-01", horarioExistente.getData());

        verify(repository).findById(1L);
        verify(repository).save(horarioExistente);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar horário inexistente - TESTE UNITÁRIO")
    void cenario10() {
        Horario horarioAtualizacao = new Horario();

        when(repository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> horarioService.update(999L, horarioAtualizacao)
        );

        assertEquals("Horário não encontrado com id 999", exception.getMessage());
        verify(repository).findById(999L);
        verify(repository, never()).save(any());
    }
}
