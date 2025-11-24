package app.quadras.dto;

import app.quadras.entity.TipoUsuario;
import app.quadras.entity.Usuario;


public record RegistroResponseDTO(Long id, String nome, String email, TipoUsuario tipoUsuario) {

    public static RegistroResponseDTO fromUsuario(Usuario usuario) {
        return new RegistroResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipoUsuario()
        );
    }
}