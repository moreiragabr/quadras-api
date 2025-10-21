package app.quadras.controller;

import app.quadras.entity.Time;
import app.quadras.entity.TipoEsporte;
import app.quadras.service.TimeService;
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
public class TimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TimeService timeService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Time time = new Time(1L, "Time Teste", null, TipoEsporte.POLIESPORTIVA, null, null, null);

    @Test
    @DisplayName("Testar findAll - TESTE DE INTEGRAÇÃO")
    void cenario01() throws Exception {
        List<Time> times = List.of(time);
        when(timeService.findAll()).thenReturn(times);

        mockMvc.perform(get("/api/time/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Time Teste"));

        verify(timeService, times(1)).findAll();
    }

    @Test
    @DisplayName("Testar findById - TESTE DE INTEGRAÇÃO")
    void cenario02() throws Exception {
        when(timeService.findById(1L)).thenReturn(time);

        mockMvc.perform(get("/api/time/findById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Time Teste"));

        verify(timeService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Testar save - TESTE DE INTEGRAÇÃO")
    void cenario03() throws Exception {
        when(timeService.save(any(Time.class))).thenReturn(time);

        mockMvc.perform(post("/api/time/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(time)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Time Teste"));

        verify(timeService, times(1)).save(any(Time.class));
    }

    @Test
    @DisplayName("Testar delete - TESTE DE INTEGRAÇÃO")
    void cenario04() throws Exception {
        doNothing().when(timeService).delete(1L);

        mockMvc.perform(delete("/api/time/delete/1"))
                .andExpect(status().isOk());

        verify(timeService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("Testar update - TESTE DE INTEGRAÇÃO")
    void cenario05() throws Exception {
        when(timeService.update(eq(1L), any(Time.class))).thenReturn(time);

        mockMvc.perform(put("/api/time/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(time)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Time Teste"));

        verify(timeService, times(1)).update(eq(1L), any(Time.class));
    }

}
