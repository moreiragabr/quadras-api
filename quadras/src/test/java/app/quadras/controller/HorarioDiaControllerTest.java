package app.quadras.controller;

import app.quadras.entity.HorarioDia;
import app.quadras.service.HorarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class HorarioDiaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HorarioService horarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private final HorarioDia horarioDia = new HorarioDia(1L, "18:00", "2023-10-10", null, null, null, null);

    @Test
    @DisplayName("Testar listarTodos - TESTE DE INTEGRAÇÃO")
    void cenario01() throws Exception {
        List<HorarioDia> horarioDias = List.of(horarioDia);
        when(horarioService.findAll()).thenReturn(horarioDias);

        mockMvc.perform(get("/api/horarioDias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].horarioDia").value("18:00"))
                .andExpect(jsonPath("$[0].data").value("2023-10-10"));

        verify(horarioService, times(1)).findAll();
    }

    @Test
    @DisplayName("Testar buscarPorId - TESTE DE INTEGRAÇÃO")
    void cenario02() throws Exception {
        when(horarioService.findById(1L)).thenReturn(horarioDia);

        mockMvc.perform(get("/api/horarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.horarioDia").value("18:00"));

        verify(horarioService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Testar criar - TESTE DE INTEGRAÇÃO")
    void cenario03() throws Exception {
        when(horarioService.save(any(HorarioDia.class))).thenReturn(horarioDia);

        mockMvc.perform(post("/api/horarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(horarioDia)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.horarioDia").value("18:00"));

        verify(horarioService, times(1)).save(any(HorarioDia.class));
    }

    @Test
    @DisplayName("Testar atualizar - TESTE DE INTEGRAÇÃO")
    void cenario04() throws Exception {
        when(horarioService.update(eq(1L), any(HorarioDia.class))).thenReturn(horarioDia);

        mockMvc.perform(put("/api/horarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(horarioDia)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.horarioDia").value("18:00"));

        verify(horarioService, times(1)).update(eq(1L), any(HorarioDia.class));
    }

    @Test
    @DisplayName("Testar deletar - TESTE DE INTEGRAÇÃO")
    void cenario05() throws Exception {
        doNothing().when(horarioService).delete(1L);

        mockMvc.perform(delete("/api/horarios/1"))
                .andExpect(status().isNoContent());

        verify(horarioService, times(1)).delete(1L);
    }
}
