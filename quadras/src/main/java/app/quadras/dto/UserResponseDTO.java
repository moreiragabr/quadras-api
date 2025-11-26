package app.quadras.dto;

import app.quadras.entity.Reserva;
import app.quadras.entity.Usuario;

import java.util.List;

/**
 * DTO (Data Transfer Object) usado para retornar dados do usuário autenticado
 * em endpoints protegidos como /usuario-atual.
 * Garante que a senha e informações internas do Spring Security fiquem ocultas.
 */
public record UserResponseDTO(Long id, String nome, String email, String role) {

    public static UserResponseDTO fromUsuario(Usuario usuario) {
        // Mapeia a Authority de volta para a Role simples para o Front-End
        String role = usuario.getAuthorities().stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .findFirst()
                .orElse("USER");

        return new UserResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                role
        );
    }
}