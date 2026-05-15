package app.quadras.controller;

import app.quadras.dto.RegistroResponseDTO;
import app.quadras.dto.UserResponseDTO;
import app.quadras.entity.Usuario;
import app.quadras.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Value("${keycloak.admin.client-id}")
    private String clientId;

    @Value("${keycloak.admin.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;
    private final AuthService authService;
    private final app.quadras.repository.UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = firstNonBlank(
                credentials.get("username"),
                credentials.get("email")
        );
        String password = firstNonBlank(
                credentials.get("password"),
                credentials.get("senha")
        );

        if (username == null || password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Informe username (ou email) e password (ou senha)"));
        }

        String tokenUrl = issuerUri.replaceAll("/+$", "") + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("username", username.trim());
        body.add("password", password);
        body.add("scope", "openid profile email");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
            Map<String, Object> tokenBody = response.getBody();
            if (tokenBody != null && tokenBody.containsKey("access_token")) {
                Map<String, Object> envelope = new HashMap<>();
                envelope.put("token", tokenBody.get("access_token"));
                
                // Buscar dados do usuário no banco local para o frontend
                usuarioRepository.findByEmail(username.trim()).ifPresent(u -> {
                    envelope.put("id", u.getId());
                    envelope.put("nome", u.getNome());
                    envelope.put("email", u.getEmail());
                    envelope.put("role", u.getTipoUsuario() != null ? u.getTipoUsuario().name() : "USER");
                });

                if (tokenBody.containsKey("refresh_token")) {
                    envelope.put("refresh_token", tokenBody.get("refresh_token"));
                }
                if (tokenBody.containsKey("expires_in")) {
                    envelope.put("expires_in", tokenBody.get("expires_in"));
                }
                return ResponseEntity.ok(envelope);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Token não retornado pelo Keycloak"));
        } catch (Exception e) {
            log.error("Erro no login: ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "error", "Credenciais inválidas ou cliente Keycloak não autorizado ao fluxo password"));
        }
    }

    private static String firstNonBlank(String a, String b) {
        if (a != null && !a.isBlank()) {
            return a;
        }
        if (b != null && !b.isBlank()) {
            return b;
        }
        return null;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Token não fornecido"));
        }

        String token = authorization.replace("Bearer ", "");
        String logoutUrl = issuerUri.replaceAll("/+$", "") + "/protocol/openid-connect/logout";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("refresh_token", token);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(logoutUrl, request, String.class);
            return ResponseEntity.ok(Map.of("message", "Logout realizado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Erro ao realizar logout no Keycloak"));
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        log.info("Recebendo registro - Email: {}, Nome: {}", usuario.getEmail(), usuario.getNome());
        try {
            RegistroResponseDTO responseDTO = authService.registrar(usuario);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Erro ao registrar usuario", e);
            return ResponseEntity.badRequest().body(Map.of("error", "Erro ao registrar usuário: " + e.getMessage()));
        }
    }

    @GetMapping("/usuario-atual")
    public ResponseEntity<?> getUsuarioAtual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Usuario) {
            Usuario usuario = (Usuario) authentication.getPrincipal();
            UserResponseDTO response = UserResponseDTO.fromUsuario(usuario);
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Não autenticado ou token inválido"));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleJsonError(HttpMessageNotReadableException ex) {
        log.error("Erro ao ler JSON: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(Map.of(
                "error", "JSON invalido ou campo obrigatorio faltando",
                "detalhe", ex.getMostSpecificCause().getMessage()
        ));
    }
}
