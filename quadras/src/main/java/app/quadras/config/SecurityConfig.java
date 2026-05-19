package app.quadras.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; script-src 'self'; style-src 'self'; frame-ancestors 'none';"))
                        .frameOptions(frame -> frame.deny())
                )
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/registro").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/quadras").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/quadras/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/usuario").hasAuthority("ROLE_SYSJEGG_ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter)));
        http.addFilterAfter(securityFilter, BearerTokenAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Defina aqui apenas os endereços do seu FRONTEND
        configuration.setAllowedOrigins(Arrays.asList(
                "https://quadras.lab.local",
                "http://localhost:4200"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // É mais seguro listar os headers ou usar "*" se a origem já estiver restrita
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // FIX: Aplicar a todos os endpoints da API
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
