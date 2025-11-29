package app.quadras.controller;

//import app.quadras.entity.Time;
import app.quadras.dto.UserResponseDTO;
import app.quadras.entity.Usuario;
import app.quadras.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/findAll")
    public ResponseEntity<List<Usuario>> findAll() {
        var result = usuarioService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
//
//    @GetMapping("/findById/{id}")
//    public ResponseEntity<Usuario> findById(@PathVariable Long id) {
//        var result = usuarioService.findById(id);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }


    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        var result = usuarioService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

   @PostMapping("/save")
    public ResponseEntity<Usuario> save(@RequestBody Usuario usuario) {
       var result = usuarioService.save(usuario);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
   }

   @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
       usuarioService.delete(id);
       return new ResponseEntity<>(null, HttpStatus.valueOf(204));
   }

    @PutMapping("/update/{id}")
    public ResponseEntity<Usuario> update(@PathVariable Long id, @RequestBody Usuario usuario) {
        var result = usuarioService.update(id, usuario);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

//    @PutMapping("/adicionarTimeProprietario")
//    public ResponseEntity<Usuario> adicionarTimesProprietarios(@RequestParam Long idUsuario, @RequestParam Long idTime) {
//        var result = usuarioService.adicionarTimesProprietarios(idTime, idUsuario);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//
//    @PutMapping("/adicionarTimeJogador")
//    public ResponseEntity<Usuario> adicionarTimesJogador(@RequestParam Long idUsuario, @RequestParam Long idTime) {
//        var result = usuarioService.adicionarTimesJogador(idTime, idUsuario);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/removerTimesProprietarios")
//    public ResponseEntity<Usuario> removerTimesProprietarios(@RequestParam Long idUsuario, @RequestParam Long idTime) {
//        var result = usuarioService.removerTimesProprietarios(idUsuario, idTime);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/removerTimesJogador")
//    public ResponseEntity<Usuario> removerTimesJogador(@RequestParam Long idUsuario, @RequestParam Long idTime) {
//        var result = usuarioService.removerTimesJogador(idUsuario, idTime);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }

    @PutMapping("/adicionarQuadraProprietario")
    public ResponseEntity<Usuario> adicionarQuadraProprietario(
            @RequestParam Long idUsuario,
            @RequestParam Long idQuadra
   ){
        var result = usuarioService.adicionarQuadraProprietario(idUsuario, idQuadra);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
