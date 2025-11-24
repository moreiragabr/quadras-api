package app.quadras.controller;

import app.quadras.dto.LoginResponseDTO;
import app.quadras.dto.RegistroResponseDTO;
import app.quadras.dto.UserResponseDTO;
import app.quadras.entity.LoginRequest;
import app.quadras.entity.Usuario;
import app.quadras.service.AuthService;
import app.quadras.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService; // Mantido, caso precise para outras operações

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        // O Controller apenas delega e trata o resultado
        Optional<LoginResponseDTO> responseOpt = authService.loginAndGenerateToken(
                loginRequest.getEmail(),
                loginRequest.getSenha()
        );

        if (responseOpt.isPresent()) {
            return ResponseEntity.ok(responseOpt.get());
        }

        // Resposta de falha de autenticação
        return ResponseEntity.status(401).body("Credenciais inválidas");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Com JWT, o "logout" é apenas uma confirmação.
        return ResponseEntity.ok().body("Logout sinalizado com sucesso (token deve ser descartado pelo cliente)");
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            // Delega a responsabilidade de registro ao Service
            RegistroResponseDTO responseDTO = authService.registrar(usuario);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao registrar usuário: " + e.getMessage());
        }
    }

    @GetMapping("/usuario-atual")
    // ⚠️ CORREÇÃO: Retorna o DTO seguro UserResponseDTO
    public ResponseEntity<?> getUsuarioAtual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // O Spring Security só chega aqui se o token for válido e o usuário estiver autenticado
        if (authentication != null && authentication.getPrincipal() instanceof Usuario) {
            Usuario usuario = (Usuario) authentication.getPrincipal();

            // Converte a Entidade para o DTO antes de enviar ao cliente
            UserResponseDTO response = UserResponseDTO.fromUsuario(usuario);
            return ResponseEntity.ok(response);
        }

        // Caso o token seja inválido ou ausente, a requisição seria barrada no SecurityFilter
        // e não chegaria aqui, retornando 401. Este trecho é apenas um fallback seguro.
        return ResponseEntity.status(401).body("Não autenticado ou token inválido");
    }

}