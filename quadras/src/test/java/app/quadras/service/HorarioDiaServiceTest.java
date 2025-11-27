//package app.quadras.service;
//
//import app.quadras.entity.HorarioDia;
//import app.quadras.entity.Quadra;
//import app.quadras.repository.HorarioRepository;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class HorarioDiaServiceTest {
//    @Mock
//    private HorarioRepository repository;
//
//    @InjectMocks
//    private HorarioService horarioService;
//
//    @Test
//    @DisplayName("Deve retornar todos os horários - TESTE UNITÁRIO")
//    void cenario01() {
//        HorarioDia horarioDia1 = new HorarioDia();
//        horarioDia1.setId(1L);
//        HorarioDia horarioDia2 = new HorarioDia();
//        horarioDia2.setId(2L);
//
//        when(repository.findAll()).thenReturn(Arrays.asList(horarioDia1, horarioDia2));
//
//        List<HorarioDia> resultado = horarioService.findAll();
//
//        assertEquals(2, resultado.size());
//        verify(repository).findAll();
//    }
//
//    @Test
//    @DisplayName("Deve encontrar horário por ID existente - TESTE UNITÁRIO")
//    void cenario02() {
//        HorarioDia horarioDia = new HorarioDia();
//        horarioDia.setId(1L);
//
//        when(repository.findById(1L)).thenReturn(Optional.of(horarioDia));
//
//        HorarioDia resultado = horarioService.findById(1L);
//
//        assertNotNull(resultado);
//        assertEquals(1L, resultado.getId());
//        verify(repository).findById(1L);
//    }
//
//    @Test
//    @DisplayName("Deve lançar exceção ao buscar horário com ID inexistente - TESTE UNITÁRIO")
//    void cenario03() {
//        when(repository.findById(999L)).thenReturn(Optional.empty());
//
//        EntityNotFoundException exception = assertThrows(
//                EntityNotFoundException.class,
//                () -> horarioService.findById(999L)
//        );
//
//        assertEquals("Horário não encontrado com id 999", exception.getMessage());
//        verify(repository).findById(999L);
//    }
//
//    @Test
//    @DisplayName("Deve salvar horário com quadra - TESTE UNITÁRIO")
//    void cenario04() {
//        Quadra quadra = new Quadra();
//        quadra.setId(1L);
//
//        HorarioDia horarioDia = new HorarioDia();
//        horarioDia.setQuadra(quadra);
//
//        HorarioDia horarioDiaSalvo = new HorarioDia();
//        horarioDiaSalvo.setId(1L);
//
//        when(repository.save(horarioDia)).thenReturn(horarioDiaSalvo);
//
//        HorarioDia resultado = horarioService.save(horarioDia);
//
//        assertNotNull(resultado);
//        assertEquals(1L, resultado.getId());
//        verify(repository).save(horarioDia);
//    }
//
//    @Test
//    @DisplayName("Deve lançar exceção ao salvar horário sem quadra - TESTE UNITÁRIO")
//    void cenario05() {
//        HorarioDia horarioDia = new HorarioDia();
//
//        RuntimeException exception = assertThrows(
//                RuntimeException.class,
//                () -> horarioService.save(horarioDia)
//        );
//
//        assertEquals("Horário precisa de uma quadra", exception.getMessage());
//        verify(repository, never()).save(any());
//    }
//
//    @Test
//    @DisplayName("Deve deletar horário existente - TESTE UNITÁRIO")
//    void cenario06() {
//        HorarioDia horarioDia = new HorarioDia();
//        horarioDia.setId(1L);
//
//        when(repository.findById(1L)).thenReturn(Optional.of(horarioDia));
//        doNothing().when(repository).delete(horarioDia);
//
//        horarioService.delete(1L);
//
//        verify(repository).findById(1L);
//        verify(repository).delete(horarioDia);
//    }
//
//    @Test
//    @DisplayName("Deve atualizar todos os campos do horário - TESTE UNITÁRIO")
//    void cenario07() {
//        Quadra quadra = new Quadra();
//        quadra.setId(2L);
//
//        HorarioDia horarioDiaExistente = new HorarioDia();
//        horarioDiaExistente.setId(1L);
//        horarioDiaExistente.setHorario("18:00");
//        horarioDiaExistente.setData("2023-01-01");
//
//        HorarioDia horarioDiaAtualizacao = new HorarioDia();
//        horarioDiaAtualizacao.setHorario("20:00");
//        horarioDiaAtualizacao.setData("2023-01-02");
//        horarioDiaAtualizacao.setQuadra(quadra);
//
//        when(repository.findById(1L)).thenReturn(Optional.of(horarioDiaExistente));
//        when(repository.save(horarioDiaExistente)).thenReturn(horarioDiaExistente);
//
//        HorarioDia resultado = horarioService.update(1L, horarioDiaAtualizacao);
//
//        assertNotNull(resultado);
//        assertEquals("20:00", horarioDiaExistente.getHorario());
//        assertEquals("2023-01-02", horarioDiaExistente.getData());
//        assertEquals(quadra, horarioDiaExistente.getQuadra());
//
//        verify(repository).findById(1L);
//        verify(repository).save(horarioDiaExistente);
//    }
//
//    @Test
//    @DisplayName("Deve manter valores originais ao atualizar com campos nulos - TESTE UNITÁRIO")
//    void cenario08() {
//        HorarioDia horarioDiaExistente = new HorarioDia();
//        horarioDiaExistente.setId(1L);
//        horarioDiaExistente.setHorario("18:00");
//        horarioDiaExistente.setData("2023-01-01");
//
//        HorarioDia horarioDiaAtualizacao = new HorarioDia();
//
//        when(repository.findById(1L)).thenReturn(Optional.of(horarioDiaExistente));
//        when(repository.save(horarioDiaExistente)).thenReturn(horarioDiaExistente);
//
//        HorarioDia resultado = horarioService.update(1L, horarioDiaAtualizacao);
//
//        assertNotNull(resultado);
//        assertEquals("18:00", horarioDiaExistente.getHorario());
//        assertEquals("2023-01-01", horarioDiaExistente.getData());
//
//        verify(repository).findById(1L);
//        verify(repository).save(horarioDiaExistente);
//    }
//
//    @Test
//    @DisplayName("Deve ignorar campos vazios ou em branco na atualização - TESTE UNITÁRIO")
//    void cenario09() {
//        HorarioDia horarioDiaExistente = new HorarioDia();
//        horarioDiaExistente.setId(1L);
//        horarioDiaExistente.setHorario("18:00");
//        horarioDiaExistente.setData("2023-01-01");
//
//        HorarioDia horarioDiaAtualizacao = new HorarioDia();
//        horarioDiaAtualizacao.setHorario("");
//        horarioDiaAtualizacao.setData("   ");
//
//        when(repository.findById(1L)).thenReturn(Optional.of(horarioDiaExistente));
//        when(repository.save(horarioDiaExistente)).thenReturn(horarioDiaExistente);
//
//        HorarioDia resultado = horarioService.update(1L, horarioDiaAtualizacao);
//
//        assertNotNull(resultado);
//        assertEquals("18:00", horarioDiaExistente.getHorario());
//        assertEquals("2023-01-01", horarioDiaExistente.getData());
//
//        verify(repository).findById(1L);
//        verify(repository).save(horarioDiaExistente);
//    }
//
//    @Test
//    @DisplayName("Deve lançar exceção ao atualizar horário inexistente - TESTE UNITÁRIO")
//    void cenario10() {
//        HorarioDia horarioDiaAtualizacao = new HorarioDia();
//
//        when(repository.findById(999L)).thenReturn(Optional.empty());
//
//        EntityNotFoundException exception = assertThrows(
//                EntityNotFoundException.class,
//                () -> horarioService.update(999L, horarioDiaAtualizacao)
//        );
//
//        assertEquals("Horário não encontrado com id 999", exception.getMessage());
//        verify(repository).findById(999L);
//        verify(repository, never()).save(any());
//    }
//}
