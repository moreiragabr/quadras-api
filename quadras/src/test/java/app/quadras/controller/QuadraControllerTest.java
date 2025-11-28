//package app.quadras.controller;
//
//import app.quadras.entity.Quadra;
//import app.quadras.entity.TipoEsporte;
//import app.quadras.service.QuadraService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@Transactional
//public class QuadraControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private QuadraService quadraService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private final Quadra quadra = new Quadra(1L, "Quadra Teste", 1000F, true, "Local Teste", TipoEsporte.AREIA, null, null);
//
//    @Test
//    @DisplayName("Testar findAll - TESTE DE INTEGRAÇÃO")
//    void cenario01() throws Exception {
//        List<Quadra> quadras = List.of(quadra);
//        when(quadraService.findAll()).thenReturn(quadras);
//
//        mockMvc.perform(get("/api/quadras"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(1L))
//                .andExpect(jsonPath("$[0].nome").value("Quadra Teste"));
//
//        verify(quadraService, times(1)).findAll();
//    }
//
//    @Test
//    @DisplayName("Testar findById - TESTE DE INTEGRAÇÃO")
//    void cenario02() throws Exception {
//        when(quadraService.findById(1L)).thenReturn(quadra);
//
//        mockMvc.perform(get("/api/quadras/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.nome").value("Quadra Teste"));
//
//        verify(quadraService, times(1)).findById(1L);
//    }
//
//    @Test
//    @DisplayName("Testar create - TESTE DE INTEGRAÇÃO")
//    void cenario03() throws Exception {
//        when(quadraService.save(any(Quadra.class))).thenReturn(quadra);
//
//        mockMvc.perform(post("/api/quadras")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(quadra)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.nome").value("Quadra Teste"));
//
//        verify(quadraService, times(1)).save(any(Quadra.class));
//    }
//
//    @Test
//    @DisplayName("Testar update - TESTE DE INTEGRAÇÃO")
//    void cenario04() throws Exception {
//        when(quadraService.update(eq(1L), any(Quadra.class))).thenReturn(quadra);
//
//        mockMvc.perform(put("/api/quadras/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(quadra)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.nome").value("Quadra Teste"));
//
//        verify(quadraService, times(1)).update(eq(1L), any(Quadra.class));
//    }
//
//    @Test
//    @DisplayName("Testar delete - TESTE DE INTEGRAÇÃO")
//    void cenario05() throws Exception {
//        doNothing().when(quadraService).delete(1L);
//
//        mockMvc.perform(delete("/api/quadras/1"))
//                .andExpect(status().isNoContent());
//
//        verify(quadraService, times(1)).delete(1L);
//    }
//}
