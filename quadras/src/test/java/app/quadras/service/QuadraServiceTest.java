package app.quadras.service;

import app.quadras.entity.Quadra;
import app.quadras.entity.TipoEsporte;
import app.quadras.repository.QuadraRepository;
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
public class QuadraServiceTest {

    @Mock
    private QuadraRepository quadraRepository;

    @InjectMocks
    private QuadraService quadraService;

    @Test
    @DisplayName("Deve retornar todas as quadras - TESTE UNITÁRIO")
    void cenario01() {
        // Configura
        Quadra quadra1 = new Quadra();
        quadra1.setId(1L);
        Quadra quadra2 = new Quadra();
        quadra2.setId(2L);

        when(quadraRepository.findAll()).thenReturn(Arrays.asList(quadra1, quadra2));

        // Executa
        List<Quadra> resultado = quadraService.findAll();

        // Verifica
        assertEquals(2, resultado.size());
        verify(quadraRepository).findAll();
    }

    @Test
    @DisplayName("Deve encontrar quadra por ID existente - TESTE UNITÁRIO")
    void cenario02() {
        // Configura
        Quadra quadra = new Quadra();
        quadra.setId(1L);

        when(quadraRepository.findById(1L)).thenReturn(Optional.of(quadra));

        // Executa
        Quadra resultado = quadraService.findById(1L);

        // Verifica
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(quadraRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar quadra com ID inexistente - TESTE UNITÁRIO")
    void cenario03() {
        // Configura
        when(quadraRepository.findById(999L)).thenReturn(Optional.empty());

        // Executa e Verifica
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> quadraService.findById(999L)
        );

        assertEquals("Quadra não encontrada com o id: 999", exception.getMessage());
        verify(quadraRepository).findById(999L);
    }

    @Test
    @DisplayName("Deve salvar quadra com sucesso - TESTE UNITÁRIO")
    void cenario04() {
        // Configura
        Quadra quadra = new Quadra();
        quadra.setNome("Quadra Teste");

        Quadra quadraSalva = new Quadra();
        quadraSalva.setId(1L);
        quadraSalva.setNome("Quadra Teste");

        when(quadraRepository.save(quadra)).thenReturn(quadraSalva);

        // Executa
        Quadra resultado = quadraService.save(quadra);

        // Verifica
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Quadra Teste", resultado.getNome());
        verify(quadraRepository).save(quadra);
    }

    @Test
    @DisplayName("Deve atualizar todos os campos da quadra - TESTE UNITÁRIO")
    void cenario05() {

        Quadra quadraExistente = new Quadra();
        quadraExistente.setId(1L);
        quadraExistente.setNome("Quadra Antiga");
        quadraExistente.setValorHora(50F);
        quadraExistente.setPartidaGravavel(false);
        quadraExistente.setLocalizacao("Local Antigo");
        quadraExistente.setTipoQuadra(TipoEsporte.AREIA);

        Quadra quadraDetails = new Quadra();
        quadraDetails.setNome("Quadra Nova");
        quadraDetails.setValorHora(100F);
        quadraDetails.setPartidaGravavel(true);
        quadraDetails.setLocalizacao("Local Novo");
        quadraDetails.setTipoQuadra(TipoEsporte.BASQUETE);

        when(quadraRepository.findById(1L)).thenReturn(Optional.of(quadraExistente));
        when(quadraRepository.save(quadraExistente)).thenReturn(quadraExistente);

        Quadra resultado = quadraService.update(1L, quadraDetails);

        assertNotNull(resultado);
        assertEquals("Quadra Nova", quadraExistente.getNome());
        assertEquals(100, quadraExistente.getValorHora());
        assertTrue(quadraExistente.getPartidaGravavel());
        assertEquals("Local Novo", quadraExistente.getLocalizacao());
        assertEquals(TipoEsporte.BASQUETE, quadraExistente.getTipoQuadra());

        verify(quadraRepository).findById(1L);
        verify(quadraRepository).save(quadraExistente);
    }

    @Test
    @DisplayName("Deve manter valores originais ao atualizar com campos nulos - TESTE UNITÁRIO")
    void cenario06() {
        Quadra quadraExistente = new Quadra();
        quadraExistente.setId(1L);
        quadraExistente.setNome("Quadra Original");
        quadraExistente.setValorHora(80F);
        quadraExistente.setPartidaGravavel(true);
        quadraExistente.setLocalizacao("Local Original");
        quadraExistente.setTipoQuadra(TipoEsporte.BASQUETE);

        Quadra quadraDetails = new Quadra();

        when(quadraRepository.findById(1L)).thenReturn(Optional.of(quadraExistente));
        when(quadraRepository.save(quadraExistente)).thenReturn(quadraExistente);

        Quadra resultado = quadraService.update(1L, quadraDetails);

        assertNotNull(resultado);
        assertEquals("Quadra Original", resultado.getNome());
        assertEquals(80F, resultado.getValorHora());
        assertTrue(resultado.getPartidaGravavel());
        assertEquals("Local Original", resultado.getLocalizacao());
        assertEquals(TipoEsporte.BASQUETE, resultado.getTipoQuadra());

        verify(quadraRepository).findById(1L);
        verify(quadraRepository).save(quadraExistente);
    }

    @Test
    @DisplayName("Deve deletar quadra existente - TESTE UNITÁRIO")
    void cenario07() {
        when(quadraRepository.existsById(1L)).thenReturn(true);
        doNothing().when(quadraRepository).deleteById(1L);

        quadraService.delete(1L);

        verify(quadraRepository).existsById(1L);
        verify(quadraRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar quadra inexistente - TESTE UNITÁRIO")
    void cenario08() {
        when(quadraRepository.existsById(999L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> quadraService.delete(999L)
        );

        assertEquals("Quadra não encontrada com o id: 999", exception.getMessage());
        verify(quadraRepository).existsById(999L);
        verify(quadraRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar quadra inexistente - TESTE UNITÁRIO")
    void cenario9() {
        Quadra quadraDetails = new Quadra();
        quadraDetails.setNome("Quadra Nova");

        when(quadraRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> quadraService.update(999L, quadraDetails)
        );

        assertEquals("Quadra não encontrada com o id: 999", exception.getMessage());
        verify(quadraRepository).findById(999L);
        verify(quadraRepository, never()).save(any());
    }

}
