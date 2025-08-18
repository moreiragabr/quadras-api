package app.quadras.controller;

import app.quadras.entity.Usuario;
import app.quadras.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> findAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Usuario> save(@RequestBody Usuario usuario) {
        return new ResponseEntity<>(usuarioService.save(usuario), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(@PathVariable Long id, @RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.update(id, usuario));
    }

    @PutMapping("/{idUsuario}/adicionar-time-proprietario/{idTime}")
    public ResponseEntity<Usuario> adicionarTimesProprietarios(@PathVariable Long idUsuario, @PathVariable Long idTime) {
        return ResponseEntity.ok(usuarioService.adicionarTimesProprietarios(idTime, idUsuario));
    }

    @PutMapping("/{idUsuario}/adicionar-time-jogador/{idTime}")
    public ResponseEntity<Usuario> adicionarTimesJogador(@PathVariable Long idUsuario, @PathVariable Long idTime) {
        return ResponseEntity.ok(usuarioService.adicionarTimesJogador(idTime, idUsuario));
    }

    @DeleteMapping("/{idUsuario}/remover-time-proprietario/{idTime}")
    public ResponseEntity<Usuario> removerTimesProprietarios(@PathVariable Long idUsuario, @PathVariable Long idTime) {
        return ResponseEntity.ok(usuarioService.removerTimesProprietarios(idUsuario, idTime));
    }

    @DeleteMapping("/{idUsuario}/remover-time-jogador/{idTime}")
    public ResponseEntity<Usuario> removerTimesJogador(@PathVariable Long idUsuario, @PathVariable Long idTime) {
        return ResponseEntity.ok(usuarioService.removerTimesJogador(idUsuario, idTime));
    }
}
