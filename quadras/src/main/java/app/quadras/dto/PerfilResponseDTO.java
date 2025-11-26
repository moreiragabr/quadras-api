package app.quadras.dto;

import app.quadras.entity.Quadra;
import app.quadras.entity.Reserva;
import app.quadras.entity.TipoUsuario;
import app.quadras.entity.Usuario;

import java.util.List;

/**
 * DTO de Resposta de Perfil de Usuário.
 * Contém apenas os dados públicos e essenciais do usuário (excluindo a senha).
 * * @param id O identificador único do usuário.
 *
 * @param nome        O nome completo do usuário.
 * @param email       O email do usuário.
 * @param tipoUsuario O tipo/role de acesso do usuário (ADMIN, USER, etc.).
 * @param quadras     A lista de quadras associadas ao usuário (se houver).
 */
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

    /**
     * Converte uma entidade Usuario (completa) em um PerfilResponseDTO (seguro).
     * Este método é a forma correta de criar o DTO a partir da entidade.
     * * @param usuario A entidade Usuario.
     *
     * @return Uma nova instância de PerfilResponseDTO.
     */
    public static PerfilResponseDTO fromUsuario(Usuario usuario) {
        // O record constrói o objeto a partir dos campos públicos do Usuario
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