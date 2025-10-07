package app.quadras.controller;

import app.quadras.entity.TipoUsuario;
import app.quadras.entity.Usuario;
import app.quadras.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    Usuario usuario = new Usuario();

    @BeforeEach
    void setUp() {
        usuario.setId(1L);
        usuario.setNome("UsuarioTeste");
        usuario.setSenha("1234");
        usuario.setEmail("teste@gmail.com");
        usuario.setTipoUsuario(TipoUsuario.ADMIN);
    }


    @Test
    @WithMockUser(username = "testuser", roles = {"ADMIN", "COMUM"})
    @DisplayName("TESTE DE INTEGRAÇÃO - Testar salvamento de novo usuário sem tipo pré-definido")
    void cenario01() throws Exception {

        usuario.setId(null);
        usuario.setTipoUsuario(null);

        mockMvc.perform(post("/api/usuario/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario))
                        .with(csrf())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoUsuario").value("COMUM")); // Verifica a regra de negócio
    }
}
