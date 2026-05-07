package app.quadras.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class KeycloakConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Value("${keycloak.admin.client-id}")
    private String clientId;

    /** Claim usado como identificador para findByEmail (ex.: preferred_username ou email). */
    @Value("${keycloak.principal-claim:preferred_username}")
    private String principalClaim;

    @Bean
    @Lazy
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setPrincipalClaimName(principalClaim);
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter(clientId));
        return converter;
    }

    static class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

        private final String clientId;

        public KeycloakRoleConverter(String clientId) {
            this.clientId = clientId;
        }

        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            List<GrantedAuthority> authorities = new ArrayList<>();

            @SuppressWarnings("unchecked")
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess != null && realmAccess.containsKey("roles")) {
                @SuppressWarnings("unchecked")
                List<String> realmRoles = (List<String>) realmAccess.get("roles");
                authorities.addAll(realmRoles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                        .collect(Collectors.toList()));
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
            if (resourceAccess != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(clientId);
                if (clientAccess != null && clientAccess.containsKey("roles")) {
                    @SuppressWarnings("unchecked")
                    List<String> clientRoles = (List<String>) clientAccess.get("roles");
                    authorities.addAll(clientRoles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                            .collect(Collectors.toList()));
                }
            }

            if (authorities.isEmpty()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_SYS-JEGG_USER"));
            }

            return authorities;
        }
    }
}
