//package app.quadras.controller;
//
//import app.quadras.entity.LoginRequest;
//import app.quadras.entity.TipoUsuario;
//import app.quadras.entity.Usuario;
//import app.quadras.service.AuthService;
//import app.quadras.service.UsuarioService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpSession;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@Transactional
//public class AuthControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private AuthService authService;
//
//    @MockitoBean
//    private UsuarioService usuarioService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private final Usuario usuario = new Usuario(1L, "Usuário Teste", "teste@email.com", "1234", TipoUsuario.ADMIN, null, null, null, null, null);
//
//    @Test
//    @DisplayName("Testar login com credenciais válidas - TESTE DE INTEGRAÇÃO")
//    void cenario01() throws Exception {
//        LoginRequest loginRequest = new LoginRequest("teste@email.com", "senha123");
//
//        when(authService.autenticar("teste@email.com", "senha123")).thenReturn(true);
//        when(usuarioService.findByEmail("teste@email.com")).thenReturn(Optional.of(usuario));
//
//        mockMvc.perform(post("/api/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.nome").value("Usuário Teste"));
//
//        verify(authService, times(1)).autenticar("teste@email.com", "senha123");
//        verify(usuarioService, times(1)).findByEmail("teste@email.com");
//    }
//
//    @Test
//    @DisplayName("Testar login com credenciais inválidas - TESTE DE INTEGRAÇÃO")
//    void cenario02() throws Exception {
//        LoginRequest loginRequest = new LoginRequest("teste@email.com", "senhaErrada");
//
//        when(authService.autenticar("teste@email.com", "senhaErrada")).thenReturn(false);
//
//        mockMvc.perform(post("/api/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isUnauthorized())
//                .andExpect(content().string("Credenciais inválidas"));
//
//        verify(authService, times(1)).autenticar("teste@email.com", "senhaErrada");
//        verify(usuarioService, never()).findByEmail(anyString());
//    }
//
//    @Test
//    @DisplayName("Testar logout - TESTE DE INTEGRAÇÃO")
//    void cenario03() throws Exception {
//        MockHttpSession session = new MockHttpSession();
//        session.setAttribute("usuarioLogado", "teste@email.com");
//
//        mockMvc.perform(post("/api/auth/logout").session(session))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Logout realizado com sucesso"));
//
//        assertTrue(session.isInvalid());
//    }
//
//    @Test
//    @DisplayName("Testar registro com sucesso - TESTE DE INTEGRAÇÃO")
//    void cenario04() throws Exception {
//        when(authService.registrar(any(Usuario.class))).thenReturn(usuario);
//
//        mockMvc.perform(post("/api/auth/registro")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(usuario)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.nome").value("Usuário Teste"));
//
//        verify(authService, times(1)).registrar(any(Usuario.class));
//    }
//
//    @Test
//    @DisplayName("Testar registro com erro - TESTE DE INTEGRAÇÃO")
//    void cenario05() throws Exception {
//        when(authService.registrar(any(Usuario.class))).thenThrow(new RuntimeException("Erro no banco"));
//
//        mockMvc.perform(post("/api/auth/registro")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(usuario)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Erro ao registrar usuário"));
//
//        verify(authService, times(1)).registrar(any(Usuario.class));
//    }
//
//    @Test
//    @DisplayName("Testar getUsuarioAtual com usuário logado - TESTE DE INTEGRAÇÃO")
//    void cenario06() throws Exception {
//        MockHttpSession session = new MockHttpSession();
//        session.setAttribute("usuarioLogado", "teste@email.com");
//
//        mockMvc.perform(get("/api/auth/usuario-atual").session(session))
//                .andExpect(status().isOk())
//                .andExpect(content().string("teste@email.com"));
//    }
//
//    @Test
//    @DisplayName("Testar getUsuarioAtual sem usuário logado - TESTE DE INTEGRAÇÃO")
//    void cenario07() throws Exception {
//        MockHttpSession session = new MockHttpSession();
//
//        mockMvc.perform(get("/api/auth/usuario-atual").session(session))
//                .andExpect(status().isUnauthorized())
//                .andExpect(content().string("Não autenticado"));
//    }
//
//    @Test
//    @DisplayName("Testar getUsuarioAtual com sessão nula - TESTE DE INTEGRAÇÃO")
//    void cenario08() throws Exception {
//        mockMvc.perform(get("/api/auth/usuario-atual"))
//                .andExpect(status().isUnauthorized())
//                .andExpect(content().string("Não autenticado"));
//    }
//
//}
