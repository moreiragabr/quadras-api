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
import java.util.List;
import java.util.Map;
import app.quadras.entity.TipoUsuario;

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
                String accessToken = (String) tokenBody.get("access_token");
                Map<String, Object> envelope = new HashMap<>();
                envelope.put("token", accessToken);
                
                // Extrair dados diretamente do JWT do Keycloak
                var decodedJWT = com.auth0.jwt.JWT.decode(accessToken);
                String emailExtracted = decodedJWT.getClaim("email").asString();
                if (emailExtracted == null) emailExtracted = decodedJWT.getClaim("preferred_username").asString();
                final String email = emailExtracted;
                
                String nomeExtracted = decodedJWT.getClaim("name").asString();
                final String nome = nomeExtracted != null ? nomeExtracted : email;
                
                var realmAccess = decodedJWT.getClaim("realm_access").asMap();
                @SuppressWarnings("unchecked")
                var rolesList = (List<String>) (realmAccess != null ? realmAccess.get("roles") : null);
                
                String roleMapped = "SYSJEGG_USER";
                if (rolesList != null) {
                    if (rolesList.contains("sys-jegg_admin")) {
                        roleMapped = "SYSJEGG_ADMIN";
                    } else if (rolesList.contains("sys-jegg_user")) {
                        roleMapped = "SYSJEGG_USER";
                    }
                }
                final String role = roleMapped;

                envelope.put("email", email);
                envelope.put("nome", nome);
                envelope.put("role", role);

                // Sincroniza/Busca no banco local
                usuarioRepository.findByEmail(email).ifPresentOrElse(u -> {
                    envelope.put("id", u.getId());
                    // Sincroniza a role no banco local para consistência
                    if (u.getTipoUsuario() == null || !u.getTipoUsuario().name().equals(role)) {
                        u.setTipoUsuario(TipoUsuario.valueOf(role));
                        usuarioRepository.save(u);
                    }
                }, () -> {
                    log.info("Usuário {} autenticado via Keycloak mas não encontrado no banco local. Criando registro...", email);
                    Usuario novoUsuario = new Usuario();
                    novoUsuario.setEmail(email);
                    novoUsuario.setNome(nome);
                    novoUsuario.setTipoUsuario(TipoUsuario.valueOf(role));
                    // A senha no banco local não será usada para login via Keycloak, 
                    // mas salvamos um valor para evitar erros de validação se houver.
                    novoUsuario.setSenha("KEYCLOAK_AUTH_" + java.util.UUID.randomUUID());
                    
                    Usuario salvo = usuarioRepository.save(novoUsuario);
                    envelope.put("id", salvo.getId());
                    log.info("Usuário criado com sucesso no banco local. ID: {}", salvo.getId());
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
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            log.error("Erro de autenticação no Keycloak: Status {}, Resposta {}", e.getStatusCode(), e.getResponseBodyAsString());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "error", "Credenciais inválidas ou cliente Keycloak não autorizado ao fluxo password",
                    "details", e.getResponseBodyAsString()));
        } catch (Exception e) {
            log.error("Erro inesperado no login: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Erro interno ao processar login"));
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

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Não autenticado"));
        }

        // Se o SecurityFilter já converteu para Usuario
        if (authentication.getPrincipal() instanceof Usuario usuario) {
            return ResponseEntity.ok(UserResponseDTO.fromUsuario(usuario));
        }
        
        // Se for o principal do JWT puro (caso o usuário não esteja no banco local)
        if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt jwt) {
            String email = jwt.getClaimAsString("email");
            if (email == null) email = jwt.getClaimAsString("preferred_username");
            String nome = jwt.getClaimAsString("name");
            
            // Mapeia roles do JWT
            String role = authentication.getAuthorities().stream()
                    .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                    .filter(r -> r.equals("SYSJEGG_ADMIN") || r.equals("SYSJEGG_USER"))
                    .findFirst()
                    .orElse("SYSJEGG_USER");

            return ResponseEntity.ok(new UserResponseDTO(null, nome != null ? nome : email, email, role));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Tipo de principal não suportado"));
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
