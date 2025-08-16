package app.quadras.Controller;

import app.quadras.Entity.Horario;
import app.quadras.Service.HorarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/horarios")
public class HorarioController {

    private final HorarioService service;

    public HorarioController(HorarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Horario>> listarTodos() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Horario> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Horario> criar(@RequestBody Horario horario) {
        return ResponseEntity.ok(service.save(horario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Horario> atualizar(@PathVariable Long id, @RequestBody Horario horario) {
        return ResponseEntity.ok(service.update(id, horario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
