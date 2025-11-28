package app.quadras.dto;

import app.quadras.entity.Quadra;
import app.quadras.entity.Reserva;
import app.quadras.entity.TipoUsuario;
import app.quadras.entity.Usuario;

import java.util.List;


public record PerfilResponseDTO(
        Long id,
        String nome,
        String email,
        TipoUsuario tipoUsuario,
        List<Quadra> quadras,
        List<Reserva> reservas,
        String cidade,
        String bairro,
        String estado,
        String rua,
        String numeroCasa,
        String cep
) {


    public static PerfilResponseDTO fromUsuario(Usuario usuario) {
        return new PerfilResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipoUsuario(),
                usuario.getQuadras(),
                usuario.getReservas(),
                usuario.getCidade(),
                usuario.getBairro(),
                usuario.getEstado(),
                usuario.getRua(),
                usuario.getNumeroCasa(),
                usuario.getCep()
        );
    }
}