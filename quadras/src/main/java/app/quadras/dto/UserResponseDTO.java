package app.quadras.dto;

import app.quadras.entity.Reserva;
import app.quadras.entity.Usuario;

import java.util.List;


public record UserResponseDTO(Long id, String nome, String email, String role) {

    public static UserResponseDTO fromUsuario(Usuario usuario) {
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