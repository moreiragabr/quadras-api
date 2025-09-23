package app.quadras.controller;

import app.quadras.entity.Quadra;
import app.quadras.service.QuadraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quadras")
public class QuadraController {

    @Autowired
    private QuadraService quadraService;

    @GetMapping
    public ResponseEntity<List<Quadra>> findAll() {
        return ResponseEntity.ok(quadraService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quadra> findById(@PathVariable Long id) {
        return ResponseEntity.ok(quadraService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Quadra> create(@RequestBody Quadra quadra) {
        return new ResponseEntity<>(quadraService.save(quadra), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quadra> update(@PathVariable Long id, @RequestBody Quadra quadraDetails) {
        return ResponseEntity.ok(quadraService.update(id, quadraDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        quadraService.delete(id);
        return ResponseEntity.noContent().build();
    }
}