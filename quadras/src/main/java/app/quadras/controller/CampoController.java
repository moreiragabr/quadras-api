package app.quadras.controller;

import app.quadras.entity.Campo;
import app.quadras.entity.Usuario;
import app.quadras.service.CampoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campo")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CampoController {

    private final CampoService campoService;

    @GetMapping("/findById/{id}")
    public ResponseEntity<Campo> findById(@PathVariable Long id) {
        var result = campoService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Campo>> findAll() {
        return ResponseEntity.ok(campoService.findAll());
    }

    @PostMapping("/save")
    public ResponseEntity<Campo> save(@RequestBody Campo campo) {
        var result = campoService.save(campo);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        campoService.delete(id);
        return new ResponseEntity<>(null, HttpStatus.valueOf(204));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Campo> update(@PathVariable Long id, @RequestBody Campo campo) {
        var result = campoService.update(id, campo);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
