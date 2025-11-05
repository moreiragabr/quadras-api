package app.quadras.service;

import app.quadras.entity.Quadra;
import app.quadras.entity.Time;
import app.quadras.entity.TipoUsuario;
import app.quadras.entity.Usuario;
import app.quadras.repository.QuadraRepository;
import app.quadras.repository.TimeRepository;
import app.quadras.repository.UsuarioRepository;
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
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TimeRepository timeRepository;

    @Mock
    private QuadraRepository quadraRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    @DisplayName("Deve retornar todos os usuários - TESTE UNITÁRIO")
    void cenario01() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(usuarioRepository).findAll();
    }

    @Test
    @DisplayName("Deve encontrar usuário por ID existente - TESTE UNITÁRIO")
    void cenario02() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Usuário Teste", resultado.getNome());
        verify(usuarioRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar usuário com ID inexistente - TESTE UNITÁRIO")
    void cenario03() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> usuarioService.findById(999L)
        );

        assertNotNull(exception);
        verify(usuarioRepository).findById(999L);
    }

    @Test
    @DisplayName("Deve encontrar usuário por email existente - TESTE UNITÁRIO")
    void cenario04() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("teste@email.com");

        when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.findByEmail("teste@email.com");

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals("teste@email.com", resultado.get().getEmail());
        verify(usuarioRepository).findByEmail("teste@email.com");
    }

    @Test
    @DisplayName("Deve retornar Optional vazio ao buscar usuário por email inexistente - TESTE UNITÁRIO")
    void cenario05() {
        when(usuarioRepository.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.findByEmail("naoexiste@email.com");

        assertFalse(resultado.isPresent());
        verify(usuarioRepository).findByEmail("naoexiste@email.com");
    }

    @Test
    @DisplayName("Deve salvar usuário com tipoUsuario nulo definindo como COMUM - TESTE UNITÁRIO")
    void cenario06() {
        Usuario usuario = new Usuario();
        usuario.setNome("Novo Usuário");
        usuario.setEmail("novo@email.com");
        usuario.setTipoUsuario(null);

        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setId(1L);
        usuarioSalvo.setNome("Novo Usuário");
        usuarioSalvo.setEmail("novo@email.com");
        usuarioSalvo.setTipoUsuario(TipoUsuario.COMUM);

        when(usuarioRepository.save(usuario)).thenReturn(usuarioSalvo);

        Usuario resultado = usuarioService.save(usuario);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(TipoUsuario.COMUM, usuario.getTipoUsuario());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("Deve salvar usuário mantendo tipoUsuario definido - TESTE UNITÁRIO")
    void cenario07() {
        Usuario usuario = new Usuario();
        usuario.setNome("Admin");
        usuario.setEmail("admin@email.com");
        usuario.setTipoUsuario(TipoUsuario.ADMIN);

        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setId(1L);
        usuarioSalvo.setNome("Admin");
        usuarioSalvo.setEmail("admin@email.com");
        usuarioSalvo.setTipoUsuario(TipoUsuario.ADMIN);

        when(usuarioRepository.save(usuario)).thenReturn(usuarioSalvo);

        Usuario resultado = usuarioService.save(usuario);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(TipoUsuario.ADMIN, usuario.getTipoUsuario());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("Deve deletar usuário existente - TESTE UNITÁRIO")
    void cenario08() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).delete(usuario);

        usuarioService.delete(1L);

        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).delete(usuario);
    }

    @Test
    @DisplayName("Deve atualizar nome e email do usuário - TESTE UNITÁRIO")
    void cenario09() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1L);
        usuarioExistente.setNome("Nome Antigo");
        usuarioExistente.setEmail("antigo@email.com");

        Usuario usuarioAtualizacao = new Usuario();
        usuarioAtualizacao.setNome("Nome Novo");
        usuarioAtualizacao.setEmail("novo@email.com");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(usuarioExistente)).thenReturn(usuarioExistente);

        Usuario resultado = usuarioService.update(1L, usuarioAtualizacao);

        assertNotNull(resultado);
        assertEquals("Nome Novo", usuarioExistente.getNome());
        assertEquals("novo@email.com", usuarioExistente.getEmail());
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).save(usuarioExistente);
    }

    @Test
    @DisplayName("Deve manter valores originais ao atualizar com campos nulos - TESTE UNITÁRIO")
    void cenario10() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1L);
        usuarioExistente.setNome("Nome Original");
        usuarioExistente.setEmail("original@email.com");

        Usuario usuarioAtualizacao = new Usuario(); // Campos nulos

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(usuarioExistente)).thenReturn(usuarioExistente);

        Usuario resultado = usuarioService.update(1L, usuarioAtualizacao);

        assertNotNull(resultado);
        assertEquals("Nome Original", usuarioExistente.getNome());
        assertEquals("original@email.com", usuarioExistente.getEmail());
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).save(usuarioExistente);
    }

    @Test
    @DisplayName("Deve adicionar time como proprietário - TESTE UNITÁRIO")
    void cenario11() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setTimesProprietarios(new ArrayList<>());

        Time time = new Time();
        time.setId(1L);
        time.setPresidente(null);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(timeRepository.findById(1L)).thenReturn(Optional.of(time));

        Usuario resultado = usuarioService.adicionarTimesProprietarios(1L, 1L);

        assertNotNull(resultado);
        assertTrue(usuario.getTimesProprietarios().contains(time));
        assertEquals(usuario, time.getPresidente());
        verify(usuarioRepository).findById(1L);
        verify(timeRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve adicionar usuário como jogador do time - TESTE UNITÁRIO")
    void cenario12() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setTimes(new ArrayList<>());

        Time time = new Time();
        time.setId(1L);
        time.setJogadores(new ArrayList<>());

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(timeRepository.findById(1L)).thenReturn(Optional.of(time));

        Usuario resultado = usuarioService.adicionarTimesJogador(1L, 1L);

        assertNotNull(resultado);
        assertTrue(usuario.getTimes().contains(time));
        assertTrue(time.getJogadores().contains(usuario));
        verify(usuarioRepository).findById(1L);
        verify(timeRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve remover time dos proprietários - TESTE UNITÁRIO")
    void cenario13() {
        Time time = new Time();
        time.setId(1L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setTimesProprietarios(new ArrayList<>(List.of(time)));

        time.setPresidente(usuario);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(timeRepository.findById(1L)).thenReturn(Optional.of(time));

        Usuario resultado = usuarioService.removerTimesProprietarios(1L, 1L);

        assertNotNull(resultado);
        assertFalse(usuario.getTimesProprietarios().contains(time));
        assertNull(time.getPresidente());
        verify(usuarioRepository).findById(1L);
        verify(timeRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve remover usuário dos jogadores do time - TESTE UNITÁRIO")
    void cenario14() {
        Time time = new Time();
        time.setId(1L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        time.setJogadores(new ArrayList<>(List.of(usuario)));
        usuario.setTimes(new ArrayList<>(List.of(time)));

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(timeRepository.findById(1L)).thenReturn(Optional.of(time));

        Usuario resultado = usuarioService.removerTimesJogador(1L, 1L);

        assertNotNull(resultado);
        assertFalse(usuario.getTimes().contains(time));
        assertFalse(time.getJogadores().contains(usuario));
        verify(usuarioRepository).findById(1L);
        verify(timeRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve adicionar quadra como proprietário - TESTE UNITÁRIO")
    void cenario15() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setQuadras(new ArrayList<>());

        Quadra quadra = new Quadra();
        quadra.setId(1L);
        quadra.setProprietario(null);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(quadraRepository.findById(1L)).thenReturn(Optional.of(quadra));

        Usuario resultado = usuarioService.adicionarQuadraProprietario(1L, 1L);

        assertNotNull(resultado);
        assertTrue(usuario.getQuadras().contains(quadra));
        assertEquals(usuario, quadra.getProprietario());
        verify(usuarioRepository).findById(1L);
        verify(quadraRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao adicionar time proprietário com usuário inexistente - TESTE UNITÁRIO")
    void cenario16() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> usuarioService.adicionarTimesProprietarios(1L, 999L)
        );

        assertNotNull(exception);
        verify(usuarioRepository).findById(999L);
        verify(timeRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao adicionar time proprietário com time inexistente - TESTE UNITÁRIO")
    void cenario17() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(timeRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> usuarioService.adicionarTimesProprietarios(999L, 1L)
        );

        assertNotNull(exception);
        verify(usuarioRepository).findById(1L);
        verify(timeRepository).findById(999L);
    }

    @Test
    @DisplayName("Deve ignorar campos vazios ou em branco na atualização - TESTE UNITÁRIO")
    void cenario18() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1L);
        usuarioExistente.setNome("Nome Original");
        usuarioExistente.setEmail("original@email.com");

        Usuario usuarioAtualizacao = new Usuario();
        usuarioAtualizacao.setNome("");
        usuarioAtualizacao.setEmail("   ");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(usuarioExistente)).thenReturn(usuarioExistente);

        Usuario resultado = usuarioService.update(1L, usuarioAtualizacao);

        assertNotNull(resultado);
        assertEquals("Nome Original", usuarioExistente.getNome());
        assertEquals("original@email.com", usuarioExistente.getEmail());
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).save(usuarioExistente);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar usuário inexistente - TESTE UNITÁRIO")
    void cenario19() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> usuarioService.delete(999L)
        );

        assertNotNull(exception);
        verify(usuarioRepository).findById(999L);
        verify(usuarioRepository, never()).delete(any());
    }
}