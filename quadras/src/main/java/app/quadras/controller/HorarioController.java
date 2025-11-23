package app.quadras.controller;

import app.quadras.entity.HorarioDia;
import app.quadras.service.HorarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/horarios")
@CrossOrigin("*")
public class HorarioController {

    private final HorarioService service;

    public HorarioController(HorarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<HorarioDia>> listarTodos() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HorarioDia> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<HorarioDia> criar(@RequestBody HorarioDia horarioDia) {
        return ResponseEntity.ok(service.save(horarioDia));
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<HorarioDia> atualizar(@PathVariable Long id, @RequestBody HorarioDia horario) {
//        return ResponseEntity.ok(service.update(id, horario));
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
