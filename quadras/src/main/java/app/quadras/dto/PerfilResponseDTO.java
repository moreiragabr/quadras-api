package app.quadras.dto;

import app.quadras.entity.Usuario;
import java.util.List;

public record PerfilResponseDTO(
        Long id,
        String nome,
        String email,
        String tipoUsuario,
        List<QuadraDTO> quadras,
        List<ReservaDTO> reservas,
        String cidade,
        String bairro,
        String estado,
        String rua,
        String numeroCasa,
        String cep
) {

    public static PerfilResponseDTO fromUsuario(Usuario usuario) {
        List<QuadraDTO> quadrasDTO = usuario.getQuadras().stream()
                .map(QuadraDTO::fromQuadra)
                .toList();

        List<ReservaDTO> reservasDTO = usuario.getReservas().stream()
                .map(ReservaDTO::fromReserva)
                .toList();

        return new PerfilResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipoUsuario().name(), // converte para USER/ADMIN
                quadrasDTO,
                reservasDTO,
                usuario.getCidade(),
                usuario.getBairro(),
                usuario.getEstado(),
                usuario.getRua(),
                usuario.getNumeroCasa(),
                usuario.getCep()
        );
    }
}
