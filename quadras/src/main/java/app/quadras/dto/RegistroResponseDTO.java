package app.quadras.dto;

import app.quadras.entity.TipoUsuario;
import app.quadras.entity.Usuario;


public record RegistroResponseDTO(Long id, String nome, String email, TipoUsuario tipoUsuario, String cidade,
                                  String bairro, String estado, String rua, String numeroCasa,
                                  String cep) {

    public static RegistroResponseDTO fromUsuario(Usuario usuario) {
        return new RegistroResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipoUsuario(),
                usuario.getCidade(),
                usuario.getBairro(),
                usuario.getEstado(),
                usuario.getRua(),
                usuario.getNumeroCasa(),
                usuario.getCep()
        );
    }
}