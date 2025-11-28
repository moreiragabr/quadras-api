//package app.quadras.service;
//
//import app.quadras.entity.Usuario;
//import app.quadras.repository.UsuarioRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class AuthServiceTest {
//
//    @Mock
//    private UsuarioRepository usuarioRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @InjectMocks
//    private AuthService authService;
//
//    @Test
//    @DisplayName("Deve autenticar usuário com credenciais corretas - TESTE UNITÁRIO")
//    void cenario01() {
//        Usuario usuario = new Usuario();
//        usuario.setSenha("senhaCriptografada");
//
//        when(usuarioRepository.findByEmail("teste@email.com"))
//                .thenReturn(Optional.of(usuario));
//        when(passwordEncoder.matches("senha123", "senhaCriptografada"))
//                .thenReturn(true);
//
//        boolean resultado = authService.autenticar("teste@email.com", "senha123");
//
//        assertTrue(resultado);
//    }
//
//    @Test
//    @DisplayName("Deve falhar autenticação com senha errada - TESTE UNITÁRIO")
//    void cenario02() {
//        Usuario usuario = new Usuario();
//        usuario.setSenha("senhaCriptografada");
//
//        when(usuarioRepository.findByEmail("teste@email.com"))
//                .thenReturn(Optional.of(usuario));
//        when(passwordEncoder.matches("senhaErrada", "senhaCriptografada"))
//                .thenReturn(false);
//
//        boolean resultado = authService.autenticar("teste@email.com", "senhaErrada");
//
//        assertFalse(resultado);
//    }
//
//    @Test
//    @DisplayName("Deve falhar autenticação com email não cadastrado - TESTE UNITÁRIO")
//    void cenario03() {
//        when(usuarioRepository.findByEmail("naoexiste@email.com"))
//                .thenReturn(Optional.empty());
//
//        boolean resultado = authService.autenticar("naoexiste@email.com", "senha123");
//
//        assertFalse(resultado);
//    }
//
//    @Test
//    @DisplayName("Deve registrar novo usuário com senha criptografada - TESTE UNITÁRIO")
//    void cenario04() {
//        Usuario usuario = new Usuario();
//        usuario.setSenha("senhaNormal");
//
//        Usuario usuarioSalvo = new Usuario();
//        usuarioSalvo.setId(1L);
//        usuarioSalvo.setSenha("senhaCriptografada");
//
//        when(passwordEncoder.encode("senhaNormal"))
//                .thenReturn("senhaCriptografada");
//        when(usuarioRepository.save(usuario))
//                .thenReturn(usuarioSalvo);
//
//        Usuario resultado = authService.registrar(usuario);
//
//        assertNotNull(resultado);
//        assertEquals(1L, resultado.getId());
//        verify(passwordEncoder).encode("senhaNormal");
//        verify(usuarioRepository).save(usuario);
//    }
//
//}
