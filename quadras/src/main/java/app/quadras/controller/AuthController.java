package app.quadras.controller;

import app.quadras.entity.LoginRequest;
import app.quadras.entity.Usuario;
import app.quadras.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,
                                   HttpSession session) {
        if (authService.autenticar(loginRequest.getEmail(), loginRequest.getSenha())) {
            session.setAttribute("usuarioLogado", loginRequest.getEmail());
            return ResponseEntity.ok().body("Login realizado com sucesso");
        }
        return ResponseEntity.status(401).body("Credenciais inválidas");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().body("Logout realizado com sucesso");
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioSalvo = authService.registrar(usuario);
            return ResponseEntity.ok(usuarioSalvo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao registrar usuário");
        }
    }

    @GetMapping("/usuario-atual")
    public ResponseEntity<?> getUsuarioAtual(HttpSession session) {
        String email = (String) session.getAttribute("usuarioLogado");
        if (email != null) {
            return ResponseEntity.ok(email);
        }
        return ResponseEntity.status(401).body("Não autenticado");
    }
}