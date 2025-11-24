//package app.quadras.controller;
//
//import app.quadras.entity.TipoUsuario;
//import app.quadras.entity.Usuario;
//import app.quadras.repository.UsuarioRepository;
//import app.quadras.service.UsuarioService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@Transactional
//public class UsuarioControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
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
//    @DisplayName("Testar findAll - TESTE DE INTEGRAÇÃO")
//    void cenario01() throws Exception {
//        List<Usuario> usuarios = List.of(usuario);
//        when(usuarioService.findAll()).thenReturn(usuarios);
//
//        mockMvc.perform(get("/api/usuario/findAll"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(1L))
//                .andExpect(jsonPath("$[0].nome").value("Usuário Teste"));
//
//        verify(usuarioService, times(1)).findAll();
//    }
//
//    @Test
//    @DisplayName("Testar findById - TESTE DE INTEGRAÇÃO")
//    void cenario02() throws Exception {
//        when(usuarioService.findById(1L)).thenReturn(usuario);
//
//        mockMvc.perform(get("/api/usuario/findById/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.nome").value("Usuário Teste"));
//
//        verify(usuarioService, times(1)).findById(1L);
//    }
//
//    @Test
//    @DisplayName("Testar save - TESTE DE INTEGRAÇÃO")
//    void cenario03() throws Exception {
//        when(usuarioService.save(any(Usuario.class))).thenReturn(usuario);
//
//        mockMvc.perform(post("/api/usuario/save")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(usuario)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(1L));
//
//        verify(usuarioService, times(1)).save(any(Usuario.class));
//    }
//
//    @Test
//    @DisplayName("Testar delete - TESTE DE INTEGRAÇÃO")
//    void cenario04() throws Exception {
//        doNothing().when(usuarioService).delete(1L);
//
//        mockMvc.perform(delete("/api/usuario/delete/1"))
//                .andExpect(status().isNoContent());
//
//        verify(usuarioService, times(1)).delete(1L);
//    }
//
//    @Test
//    @DisplayName("Testar update - TESTE DE INTEGRAÇÃO")
//    void cenario05() throws Exception {
//        when(usuarioService.update(eq(1L), any(Usuario.class))).thenReturn(usuario);
//
//        mockMvc.perform(put("/api/usuario/update/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(usuario)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L));
//
//        verify(usuarioService, times(1)).update(eq(1L), any(Usuario.class));
//    }
//
//    @Test
//    @DisplayName("Testar adicionarTimesProprietarios - TESTE DE INTEGRAÇÃO")
//    void cenario06() throws Exception {
//        when(usuarioService.adicionarTimesProprietarios(1L, 1L)).thenReturn(usuario);
//
//        mockMvc.perform(put("/api/usuario/adicionarTimeProprietario")
//                        .param("idUsuario", "1")
//                        .param("idTime", "1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L));
//
//        verify(usuarioService, times(1)).adicionarTimesProprietarios(1L, 1L);
//    }
//
//    @Test
//    @DisplayName("Testar adicionarTimesJogador - TESTE DE INTEGRAÇÃO")
//    void cenario07() throws Exception {
//        when(usuarioService.adicionarTimesJogador(1L, 1L)).thenReturn(usuario);
//
//        mockMvc.perform(put("/api/usuario/adicionarTimeJogador")
//                        .param("idUsuario", "1")
//                        .param("idTime", "1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L));
//
//        verify(usuarioService, times(1)).adicionarTimesJogador(1L, 1L);
//    }
//
//    @Test
//    @DisplayName("Testar removerTimesProprietarios - TESTE DE INTEGRAÇÃO")
//    void cenario08() throws Exception {
//        when(usuarioService.removerTimesProprietarios(1L, 1L)).thenReturn(usuario);
//
//        mockMvc.perform(delete("/api/usuario/removerTimesProprietarios")
//                        .param("idUsuario", "1")
//                        .param("idTime", "1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L));
//
//        verify(usuarioService, times(1)).removerTimesProprietarios(1L, 1L);
//    }
//
//    @Test
//    @DisplayName("Testar removerTimesJogador - TESTE DE INTEGRAÇÃO")
//    void cenario09() throws Exception {
//        when(usuarioService.removerTimesJogador(1L, 1L)).thenReturn(usuario);
//
//        mockMvc.perform(delete("/api/usuario/removerTimesJogador")
//                        .param("idUsuario", "1")
//                        .param("idTime", "1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L));
//
//        verify(usuarioService, times(1)).removerTimesJogador(1L, 1L);
//    }
//
//    @Test
//    @DisplayName("Testar adicionarQuadraProprietario - TESTE DE INTEGRAÇÃO")
//    void cenario10() throws Exception {
//        when(usuarioService.adicionarQuadraProprietario(1L, 1L)).thenReturn(usuario);
//
//        mockMvc.perform(put("/api/usuario/adicionarQuadraProprietario")
//                        .param("idUsuario", "1")
//                        .param("idQuadra", "1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L));
//
//        verify(usuarioService, times(1)).adicionarQuadraProprietario(1L, 1L);
//    }
//}
