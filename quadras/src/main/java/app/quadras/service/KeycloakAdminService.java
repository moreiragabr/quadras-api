package app.quadras.service;

import app.quadras.entity.TipoUsuario;
import app.quadras.entity.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class KeycloakAdminService {

    private final RestTemplate restTemplate;

    @Value("${keycloak.server-url:http://localhost:8080}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm:sys-jegg_admin}")
    private String realm;

    @Value("${keycloak.admin.client-id}")
    private String clientId;

    @Value("${keycloak.admin.client-secret}")
    private String clientSecret;

    public KeycloakAdminService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void createUser(Usuario usuario) {
        String adminToken = getAdminAccessToken();

        String adminUrl = keycloakServerUrl + "/admin/realms/" + realm + "/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);

        Map<String, Object> userPayload = new LinkedHashMap<>();
        userPayload.put("username", usuario.getEmail());
        userPayload.put("email", usuario.getEmail());
        userPayload.put("enabled", true);
        userPayload.put("emailVerified", true);
        userPayload.put("firstName", usuario.getNome());
        userPayload.put("lastName", usuario.getTipoUsuario() != null ? usuario.getTipoUsuario().getRole() : "user");

        Map<String, Object> credential = new HashMap<>();
        credential.put("type", "password");
        credential.put("temporary", false);
        credential.put("value", usuario.getSenha());
        userPayload.put("credentials", Collections.singletonList(credential));

        Map<String, String> attributes = new HashMap<>();
        attributes.put("cidade", usuario.getCidade() != null ? usuario.getCidade() : "");
        attributes.put("bairro", usuario.getBairro() != null ? usuario.getBairro() : "");
        attributes.put("estado", usuario.getEstado() != null ? usuario.getEstado() : "");
        attributes.put("rua", usuario.getRua() != null ? usuario.getRua() : "");
        attributes.put("numeroCasa", usuario.getNumeroCasa() != null ? usuario.getNumeroCasa() : "");
        attributes.put("cep", usuario.getCep() != null ? usuario.getCep() : "");
        userPayload.put("attributes", attributes);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(userPayload, headers);

        ResponseEntity<Void> response = restTemplate.exchange(adminUrl, HttpMethod.POST, request, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Falha ao criar usuario no Keycloak: " + response.getStatusCode());
        }

        assignRoleToUser(usuario.getEmail(), usuario.getTipoUsuario());
    }

    public void assignRoleToUser(String email, TipoUsuario tipoUsuario) {
        String adminToken = getAdminAccessToken();

        String userId = getUserIdByEmail(email, adminToken);
        if (userId == null) {
            log.warn("Usuario nao encontrado no Keycloak para atribuicao de role: {}", email);
            return;
        }

        String roleName = tipoUsuario != null ? tipoUsuario.getRole() : "sys-jegg_user";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);

        String roleUrl = keycloakServerUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm";

        List<Map<String, Object>> rolePayload = Collections.singletonList(Map.of("name", roleName));

        HttpEntity<List<Map<String, Object>>> request = new HttpEntity<>(rolePayload, headers);

        try {
            ResponseEntity<Void> response = restTemplate.exchange(roleUrl, HttpMethod.POST, request, Void.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Role {} atribuida ao usuario {} com sucesso", roleName, email);
            }
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("A role '{}' nao foi encontrada no Keycloak. Verifique se ela existe no realm '{}' e se e uma 'Realm Role'.", roleName, realm);
        } catch (Exception e) {
            log.warn("Falha ao atribuir role " + roleName + " ao usuario " + email + ": " + e.getMessage());
        }
    }

    private String getUserIdByEmail(String email, String adminToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        String searchUrl = keycloakServerUrl + "/admin/realms/" + realm + "/users?email=" + email;

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(searchUrl, HttpMethod.GET, request, List.class);

        if (response.getBody() != null && !response.getBody().isEmpty()) {
            @SuppressWarnings("unchecked")
            Map<String, Object> user = (Map<String, Object>) response.getBody().get(0);
            return (String) user.get("id");
        }

        return null;
    }

    private String getAdminAccessToken() {
        String tokenUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        if (response.getBody() != null && response.getBody().containsKey("access_token")) {
            return (String) response.getBody().get("access_token");
        }

        throw new RuntimeException("Falha ao obter token de admin do Keycloak");
    }
}
